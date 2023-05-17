package data

import domain.interfaces.LabelCreationService
import domain.interfaces.TestRepository

class TestRepositoryImpl(
    private val labelCreationService: LabelCreationService
): TestRepository {
    override fun testFunction() {
        TODO("Not yet implemented")
    }
}