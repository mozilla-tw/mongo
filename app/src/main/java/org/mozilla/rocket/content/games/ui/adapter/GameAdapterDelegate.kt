package org.mozilla.rocket.content.games.ui.adapter

import android.content.Intent
import android.view.View
import kotlinx.android.synthetic.main.item_game.image
import kotlinx.android.synthetic.main.item_game.name
import org.mozilla.focus.R
import org.mozilla.focus.glide.GlideApp
import org.mozilla.rocket.adapter.AdapterDelegate
import org.mozilla.rocket.adapter.DelegateAdapter
import org.mozilla.rocket.content.games.ui.GamesViewModel
import android.view.ContextMenu
import android.view.MotionEvent
import org.mozilla.rocket.content.games.vo.Game

class GameAdapterDelegate(private val gamesViewModel: GamesViewModel) : AdapterDelegate {
    override fun onCreateViewHolder(view: View): DelegateAdapter.ViewHolder =
        GameViewHolder(view, gamesViewModel)
}

class GameViewHolder(
    override val containerView: View,
    private val gamesViewModel: GamesViewModel
) : DelegateAdapter.ViewHolder(containerView), View.OnCreateContextMenuListener {
    override fun bind(uiModel: DelegateAdapter.UiModel) {
        var gameItem = uiModel as Game
        name.text = gameItem.name
        GlideApp.with(itemView.context)
            .asBitmap()
            .placeholder(R.drawable.placeholder)
            .fitCenter()
            .load(gameItem.imageUrl)
            .into(image)

        itemView.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN -> gamesViewModel.onGameItemTouched(gameItem)
                }
                return v?.onTouchEvent(event) ?: true
            }
        })
        itemView.setOnClickListener { gamesViewModel.onGameItemClicked(gameItem) }
        itemView.setOnLongClickListener { gamesViewModel.onGameItemTouched(gameItem) }
        itemView.setOnCreateContextMenuListener(this)
    }

    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        menu?.setHeaderTitle(name.text)
        val intent = Intent()
        intent.putExtra("gameType", gamesViewModel.selectedGame.type)
        if (gamesViewModel.canShare())
            menu?.add(0, R.id.share, 0, R.string.gaming_vertical_menu_option_1)?.setIntent(intent)
        if (gamesViewModel.canCreateShortCut())
            menu?.add(0, R.id.shortcut, 0, R.string.gaming_vertical_menu_option_2)?.setIntent(intent)
        if (gamesViewModel.canRemoveFromList())
            menu?.add(0, R.id.remove, 0, R.string.gaming_vertical_menu_option_3)?.setIntent(intent)
    }
}