package domain.models

data class BarcodeImage(
    val imageData: ByteArray,
    val width: Int,
    val height: Int
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as BarcodeImage

        if (!imageData.contentEquals(other.imageData)) return false
        if (width != other.width) return false
        return height == other.height
    }

    override fun hashCode(): Int {
        var result = imageData.contentHashCode()
        result = 31 * result + width
        result = 31 * result + height
        return result
    }
}
