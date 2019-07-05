package it.earthgardeners.alien

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import it.earthgardeners.alien.models.Animal
import it.earthgardeners.alien.models.Habitat
import it.earthgardeners.alien.models.Plant

/**
 * This class is part of Alien project.
 * Created by riccardopizzoni on 2019-07-05.
 * Copyright © 2019 INDAPP
 * info@indapp.it
 */
object AlienRepository {
    var habitats: List<Habitat> = emptyList()
        private set
    var plants: List<Plant> = emptyList()
        private set
    var animals: List<Animal> = emptyList()
        private set

    private var syncCount = 0
    private var completion: (()->Unit)? = null

    fun sync(completion: () -> Unit) {
        this.syncCount = 3
        this.completion = completion

        val db = FirebaseFirestore.getInstance()
        val habitatCollection = db.collection("habitat")
        val plantCollection = db.collection("plant")
        val animalCollection = db.collection("animal")

        habitatCollection.addSnapshotListener(this::onHabitatListUpdate)
        plantCollection.addSnapshotListener(this::onPlantListUpdate)
        animalCollection.addSnapshotListener(this::onAnimalListUpdate)
    }

    private fun onHabitatListUpdate(querySnapshot: QuerySnapshot?, error: FirebaseFirestoreException?) {
        val snapshots = querySnapshot?.documents ?: return checkCompletion()
        habitats = snapshots.mapNotNull { snapshot -> snapshot.toObject(Habitat::class.java) }
        checkCompletion()
    }

    private fun onPlantListUpdate(querySnapshot: QuerySnapshot?, error: FirebaseFirestoreException?) {
        val snapshots = querySnapshot?.documents ?: return checkCompletion()
        plants = snapshots.mapNotNull { snapshot -> snapshot.toObject(Plant::class.java) }
        checkCompletion()
    }

    private fun onAnimalListUpdate(querySnapshot: QuerySnapshot?, error: FirebaseFirestoreException?) {
        val snapshots = querySnapshot?.documents ?: return checkCompletion()
        animals = snapshots.mapNotNull { snapshot -> snapshot.toObject(Animal::class.java) }
        checkCompletion()
    }

    fun checkCompletion() {
        syncCount--
        if (syncCount <= 0) {
            completion?.invoke()
            syncCount = 0
            completion = null
        }
    }

}