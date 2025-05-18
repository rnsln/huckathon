package com.example.huckathon.ui
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.huckathon.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChatBotFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_chat_bot, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val input = view.findViewById<EditText>(R.id.messageInput)
        val button = view.findViewById<Button>(R.id.sendButton)
        val chatContainer = view.findViewById<LinearLayout>(R.id.chatContainer)
        val scrollView = view.findViewById<ScrollView>(R.id.chatScroll)

        button.setOnClickListener {
            val msg = input.text.toString().trim()
            if (msg.isEmpty()) return@setOnClickListener

            addUserMessage(chatContainer, msg)
            scrollToBottom(scrollView)

            input.setText("")

            // API CALL
            val request = MessageRequest(msg)
            RetrofitInstance.api.sendMessage(request).enqueue(object : Callback<ChatBotResponse> {
                override fun onResponse(call: Call<ChatBotResponse>, response: Response<ChatBotResponse>) {
                    val reply = response.body()?.response ?: "Boş cevap"
                    addBotMessage(chatContainer, reply)
                    scrollToBottom(scrollView)
                }

                override fun onFailure(call: Call<ChatBotResponse>, t: Throwable) {
                    addBotMessage(chatContainer, "Bağlantı hatası: ${t.localizedMessage}")
                    scrollToBottom(scrollView)
                }
            })
        }
    }
    private fun addUserMessage(container: LinearLayout, text: String) {
        val userLayout = layoutInflater.inflate(R.layout.chat_user_message, container, false)
        userLayout.findViewById<TextView>(R.id.userMessageText).text = text
        container.addView(userLayout)
    }

    private fun addBotMessage(container: LinearLayout, text: String) {
        val botLayout = layoutInflater.inflate(R.layout.chat_bot_message, container, false)
        botLayout.findViewById<TextView>(R.id.botMessageText).text = text
        container.addView(botLayout)
    }

    private fun scrollToBottom(scrollView: ScrollView) {
        scrollView.post { scrollView.fullScroll(View.FOCUS_DOWN) }
    }


}
