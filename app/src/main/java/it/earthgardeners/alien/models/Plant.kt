package it.earthgardeners.alien.models

/**
 * This class is part of Alien project.
 * Created by riccardopizzoni on 2019-07-05.
 * Copyright © 2019 INDAPP
 * info@indapp.it
 */

data class Plant(
    override val name: String? = null,
    override val tag: String = "",
    override val description: String? = null,
    override val latin: String? = null,
    override val alien: List<String> = emptyList(),
    override val habitat: List<String> = emptyList()
): Creature {
    override val type: Creature.Type
        get() = Creature.Type.PLANT
}