package com.screenmemo.premium.events

import com.screenmemo.premium.presentor.MemoListFragment
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by longfan on 2019/2/24.
 */
object MemoFactory {
  private val simpleFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US)//如2016-08-10 20:40
  private val memos = ArrayList<Memo>()

  init {
    reset()
  }

  fun reset() {
    memos.clear()
    memos.add(Memo("", "14:46", "去吃饭"))
    memos.add(Memo("", "18:01", "去旅游"))
    memos.add(Memo("", "18:26", "去吃饭啦"))
  }

  fun getPresentMemos(): List<Memo> {
    return filter(memos)
  }

  fun removeMemo(memo: Memo) {
    memos.remove(memo)
  }

  private fun filter(data: List<Memo>): List<Memo> {
    val result = ArrayList<Memo>()
    for (memo in data) {
      val currentDateTime = simpleFormat.format(Date())
      val currentDate = currentDateTime.subSequence(0, 10)
      val currentTime = currentDateTime.subSequence(11, 16)
      val toDate = "${if (memo.date.isNotBlank()) memo.date else currentDate} " +
          "${if (memo.time.isNotBlank()) memo.time else currentTime}"
      val from = System.currentTimeMillis()
      val to = simpleFormat.parse(toDate).time
      val minutes = ((to - from) / (1000 * 60))
      if (Math.abs(minutes) < 10) {
        result.add(memo)
      }
    }
    return result
  }
}