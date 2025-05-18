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
        val output = view.findViewById<TextView>(R.id.responseText)

        button.setOnClickListener {
            val msg = input.text.toString()
            val request = MessageRequest(msg)

            RetrofitInstance.api.sendMessage(request).enqueue(object : Callback<ChatBotResponse> {
                override fun onResponse(call: Call<ChatBotResponse>, response: Response<ChatBotResponse>) {
                    if (response.isSuccessful) {
                        output.text = response.body()?.response ?: "Boş cevap"
                    } else {
                        output.text = "Sunucu hatası: ${response.code()}"
                    }
                }

                override fun onFailure(call: Call<ChatBotResponse>, t: Throwable) {
                    output.text = "Bağlantı hatası: ${t.localizedMessage}"
                }
            })
        }
    }
}
