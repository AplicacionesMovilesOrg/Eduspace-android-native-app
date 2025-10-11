package upc.edu.pe.eduspace.features.menu.domain

import upc.edu.pe.eduspace.features.menu.domain.repository.MenuRepository

class GetMenuUseCase(private val repo: MenuRepository) {
    operator fun invoke() = repo.getMenu()
}