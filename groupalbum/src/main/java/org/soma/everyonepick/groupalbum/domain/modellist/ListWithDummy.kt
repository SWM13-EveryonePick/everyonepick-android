package org.soma.everyonepick.groupalbum.domain.modellist

/**
 * [data]의 마지막 아이템에 항상 dummyData가 위치하는 것을 보장하는 클래스입니다.
 */
interface ListWithDummy<T> {
    val data: List<T>
    fun getItemCountWithoutDummy(): Int
    fun getListWithoutDummy(): List<T>
}