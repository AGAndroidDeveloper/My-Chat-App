import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.*
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ActionMode
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.ankitgupta.dev.mychatapp.R
import com.ankitgupta.dev.mychatapp.databinding.RecieveMessageBinding
import com.ankitgupta.dev.mychatapp.databinding.SendMessageBinding
import com.ankitgupta.dev.mychatapp.model.Message
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class ChatAdapter(
    private val context: Context,
    private val list: ArrayList<Message>,
    private val listener: MessageAdapterListener,
   private val toolbar: Toolbar?
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var actionMode: ActionMode? = null
    private val selectedMessages = mutableListOf<Int>()
    private var isSelectionMode = false

    inner class SentMessageViewHolder(binding: SendMessageBinding) : RecyclerView.ViewHolder(binding.root) {
        val sendMessage: TextView = binding.sendMessage
    }

    inner class ReceivedMessageViewHolder(binding: RecieveMessageBinding) : RecyclerView.ViewHolder(binding.root) {
        val receiveMessage: TextView = binding.recieveMessage
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == SENT_MESSAGE) {
            val binding = SendMessageBinding.inflate(inflater, parent, false)
            SentMessageViewHolder(binding)
        } else {
            val binding = RecieveMessageBinding.inflate(inflater, parent, false)
            ReceivedMessageViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val data = list[position]
        if (holder is SentMessageViewHolder) {
            holder.sendMessage.text = data.content
        } else if (holder is ReceivedMessageViewHolder) {
            holder.receiveMessage.text = data.content
        }

        holder.itemView.setOnLongClickListener { view ->
            if (actionMode == null) {

toolbar!!.visibility = View.GONE
                actionMode = (context as AppCompatActivity).startSupportActionMode(actionModeCallback)
                toggleSelection(position)
                true
            } else {
                false
            }
        }

        if (isSelectionMode) {
            if (selectedMessages.contains(position)) {
                holder.itemView.setBackgroundResource(R.drawable.selected_background)
            } else {
                holder.itemView.background = null
            }
        } else {
            holder.itemView.background = null
        }
    }

    private val actionModeCallback = object : ActionMode.Callback {
        override fun onCreateActionMode(mode: ActionMode, menu: Menu): Boolean {
            val inflater: MenuInflater = mode.menuInflater
            inflater.inflate(R.menu.menu12, menu)
            val actionModeToolbar = mode.customView?.parent as? Toolbar

            // Set the background color
            actionModeToolbar?.setBackgroundColor(ContextCompat.getColor(context,R.color.colorPrimaryDark))

            return true
        }

        override fun onPrepareActionMode(mode: ActionMode, menu: Menu): Boolean {
            return false
        }

        override fun onActionItemClicked(mode: ActionMode, item: MenuItem): Boolean {
            return when (item.itemId) {
                R.id.delete -> {
                    deleteSelectedItems()
                    mode.finish()
                    true
                }
                else -> false
            }
        }

        override fun onDestroyActionMode(mode: ActionMode) {
            actionMode = null
            toolbar?.visibility = View.VISIBLE
            clearSelection()
        }
    }

    private fun toggleSelection(position: Int) {
        if (selectedMessages.contains(position)) {
            selectedMessages.remove(position)
        } else {
            selectedMessages.add(position)
        }
        notifyDataSetChanged()
        listener.onMessageLongPressed()
    }

    private fun clearSelection() {
        selectedMessages.clear()
        notifyDataSetChanged()
        listener.onMessageLongPressed()
    }


    private fun deleteSelectedItems() {

        val selectedItems = selectedMessages.sortedDescending()
        Log.e("seItem",selectedItems.toString())
        for (position in selectedItems) {
            list.removeAt(position)
            notifyDataSetChanged()
        }
        clearSelection()
    }

    override fun getItemViewType(position: Int): Int {
        return if (FirebaseAuth.getInstance().currentUser!!.uid == list[position].senderId) {
            SENT_MESSAGE
        } else {
            RECEIVE_MESSAGE
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface MessageAdapterListener {
        fun onMessageLongPressed()
    }

    companion object {
        private const val SENT_MESSAGE = 1
        private const val RECEIVE_MESSAGE = 2
    }
}
