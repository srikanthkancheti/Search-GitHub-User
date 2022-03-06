package android.com.viper.app.adapter

import android.com.viper.R.layout
import android.com.viper.app.viewHolder.ListItemViewHolder
import android.com.viper.model.response.GitHubSearchResultItem
import android.com.viper.ui.helper.OnBottomReachedListener
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedList
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView.ViewHolder

class SearchResultsRecyclerAdapter(
  private val context: Context,
  private val onBottomReachedListener: OnBottomReachedListener,
  private val itemClickListener: (GitHubSearchResultItem) -> Unit
) : PagedListAdapter<GitHubSearchResultItem, ViewHolder>(IMAGES_COMPARATOR) {

  private var listOfSearchResults: PagedList<GitHubSearchResultItem>? = null

  override fun submitList(pagedList: PagedList<GitHubSearchResultItem>?) {
    listOfSearchResults = pagedList
    notifyDataSetChanged()
    super.submitList(pagedList)
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    return ListItemViewHolder(LayoutInflater.from(parent.context).inflate(layout.list_item_search_github, parent, false))
  }

  override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
    val catsListViewHolder = viewHolder as ListItemViewHolder
    listOfSearchResults?.get(position)?.let { catsListViewHolder.bindView(it, itemClickListener) }

    if (position == (listOfSearchResults?.size)?.minus(1)) {
      onBottomReachedListener.onBottomReached()
    }
  }

  override fun onViewRecycled(holder: ViewHolder) {
    if (holder is ListItemViewHolder) {
      holder.cancelImageLoading()
    }
  }

  companion object {
    val IMAGES_COMPARATOR = object : DiffUtil.ItemCallback<GitHubSearchResultItem>() {
      override fun areItemsTheSame(oldItem: GitHubSearchResultItem, newItem: GitHubSearchResultItem): Boolean =
        oldItem != newItem

      override fun areContentsTheSame(oldItem: GitHubSearchResultItem, newItem: GitHubSearchResultItem): Boolean =
        oldItem.id != newItem.id
    }
  }
}
