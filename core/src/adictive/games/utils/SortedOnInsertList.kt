package adictive.games.utils


import java.util.*

class SortedOnInsertList<E>(initialAllocation: Int, private val comparator: Comparator<E>) : ArrayList<E>(initialAllocation) {

    override fun add(index: Int, element: E) {
        throw RuntimeException("Do not insert by index on a SortedOnInsertList")
    }

    override fun add(e: E): Boolean {
        val result = super.add(e)
        Collections.sort(this, this.comparator)
        return result
    }
}