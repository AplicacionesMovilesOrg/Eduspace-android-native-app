package upc.edu.pe.eduspace.features.menu.domain.repository

import upc.edu.pe.eduspace.features.menu.domain.model.MenuEntry


interface MenuRepository {
    fun getMenu(): List<MenuEntry>
}