package it.earthgardeners.alien.models

/**
 * This class is part of Alien project.
 * Created by riccardopizzoni on 2019-07-05.
 * Copyright © 2019 INDAPP
 * info@indapp.it
 */

data class Plant(
    override val name: String,
    override val tag: String,
    override val description: String,
    override val alien: List<String>,
    override val habitat: List<String>
): Creature