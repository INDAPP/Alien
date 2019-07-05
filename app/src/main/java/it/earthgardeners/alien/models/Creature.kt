package it.earthgardeners.alien.models

/**
 * This class is part of Alien project.
 * Created by riccardopizzoni on 2019-07-05.
 * Copyright © 2019 INDAPP
 * info@indapp.it
 */
interface Creature {
    val name: String
    val tag: String
    val description: String
    val alien: List<String>
    val habitat: List<String>
}