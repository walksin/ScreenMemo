package com.screenmemo.premium.presentor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.screenmemo.premium.R
import com.screenmemo.premium.events.Memo
import com.screenmemo.premium.events.MemoFactory
import kotlinx.android.synthetic.main.fragment_memolist.*
import java.util.concurrent.TimeUnit


/**
 * Created by longfan on 2019/2/24.
 */
class MemoListFragment : Fragment() {

  private val memos: ArrayList<Memo> = ArrayList()
  private val adapter = MemoAdapter()
  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    return inflater.inflate(R.layout.fragment_memolist, container, false)
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    showMemos()
  }

  private val updateMemos = Runnable {
    memos.clear()
    memos.addAll(MemoFactory.getPresentMemos())
    adapter.data.clear()
    adapter.data.addAll(memos)
    adapter.notifyDataSetChanged()
    startCheckMemos()
  }

  private fun showMemos() {
    memos.addAll(MemoFactory.getPresentMemos())
    adapter.data.addAll(memos)
    recyclerView.layoutManager = LinearLayoutManager(context)
    recyclerView.adapter = adapter
    startCheckMemos()
  }

  private fun startCheckMemos() {
    recyclerView.removeCallbacks(updateMemos)
    recyclerView.postDelayed(updateMemos, TimeUnit.SECONDS.toMillis(5))
  }

  class MemoAdapter : RecyclerView.Adapter<MemoAdapter.MemoViewHolder>() {
    val data: ArrayList<Memo> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemoViewHolder {
      return MemoViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_memo, parent, false))
    }

    override fun getItemCount(): Int {
      return data.size
    }

    override fun onBindViewHolder(holder: MemoViewHolder, position: Int) {
      holder.render(data[position], position)
    }

    inner class MemoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
      fun render(memo: Memo, position: Int) {
        itemView.findViewById<TextView>(R.id.title).text = memo.event
        itemView.findViewById<TextView>(R.id.time).text = memo.time
        itemView.setOnClickListener {
          data.remove(memo)
          notifyItemRemoved(position)
          MemoFactory.removeMemo(memo)
        }
      }
    }
  }
}