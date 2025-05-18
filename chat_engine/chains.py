from langchain_openai import ChatOpenAI
from langchain.prompts import PromptTemplate

class notification_chain():
    def __init__(self, api_key):
        self.llm = ChatOpenAI(
            openai_api_key=api_key,
            model="gpt-4.1-nano",
            temperature=0.3
        )

        self.template = """
Sen bir günlük yaşam asistanısın. Kullanıcın Lümen türünde bir uzaylı. Bu uzaylıya sadece verilerini analiz ederek ilgili konularda önerilerde bulun. Önemli verileri göz önüne al ve bu Lümen'e bir hatırlatma mesajı oluştur. Ona durumu anlat ve bir şeyler yapmasını öner, nedenini anlat. Cevapların kısa ve öz olsun. Net önerilerde bulun

Kullanıcı verileri: {user_input}
Üzerinde öneri yapılacak konu: {user_data}
Öneri:
        """

        self.prompt = PromptTemplate(input_variables=["user_input", "user_data"], template=self.template)
        self.chain = self.prompt | self.llm

    def chat_with_qa(self, user_input, user_data):
        """
        Simply takes user input, adds it to the fixed prompt, and returns LLM response
        
        Args:
            user_input (str): User's message
            
        Returns:
            str: LLM response
        """
        response = self.chain.invoke({
            "user_input": user_input,
            "user_data": user_data})
        return response
    
class interactive_chain():
    def __init__(self, api_key):
        self.llm = ChatOpenAI(
            openai_api_key=api_key,
            model="gpt-4.1-nano",
            temperature=0.3
        )
        
        self.conversation_history = []
        
        self.max_history_length = 5
        
        self.template = """
Sen bir günlük yaşam asistanısın. Senin adın Flux. Kullanıcıların Lümin isimli bir uzaylı türüne ait olduğunu biliyorsun. Kullanıcıların verilerini analiz ederek onların sorularını cevaplayacaksın. Soruları cevaplarken onların uzsylı olduğunu göz önüne al. Verilere dikkat ederek cevap ver. Özetle, çok uzatma.

KULLANICI VERİLERİ:
{user_data}

KONUŞMA GEÇMİŞİ:
{conversation_history}

Kullanıcı: {current_message}
Flux:
        """
        
        self.prompt = PromptTemplate(
            input_variables=["user_data", "conversation_history", "current_message"], 
            template=self.template
        )
        self.chain = self.prompt | self.llm
    
    def add_to_history(self, user_message, assistant_response):
        """
        Konuşma geçmişine yeni bir etkileşim ekler ve maksimum uzunluğu korur
        
        Args:
            user_message (str): Kullanıcı mesajı
            assistant_response (str): Asistanın yanıtı
        """
        self.conversation_history.append({
            "user": user_message,
            "assistant": assistant_response
        })
        
        if len(self.conversation_history) > self.max_history_length:
            self.conversation_history = self.conversation_history[-self.max_history_length:]

    def format_conversation_history(self):
        """
        Konuşma geçmişini prompt için uygun formata dönüştürür
        
        Returns:
            str: Formatlı konuşma geçmişi
        """
        formatted_history = ""
        for entry in self.conversation_history:
            formatted_history += f"Kullanıcı: {entry['user']}\n"
            formatted_history += f"Lümin Asistan: {entry['assistant']}\n\n"
        return formatted_history
    
    def chat_with_qa(self, user_message, user_data):
        """
        Kullanıcı mesajını ve kullanıcı verilerini alıp yanıt verir
        
        Args:
            user_message (str): Kullanıcının mevcut mesajı
            user_data (str): Kullanıcı hakkında bilgiler
            
        Returns:
            str: Asistan yanıtı
        """
        formatted_history = self.format_conversation_history()
        
        response = self.chain.invoke({
            "user_data": user_data,
            "conversation_history": formatted_history,
            "current_message": user_message
        })
        
        response_content = response.content
        
        self.add_to_history(user_message, response_content)
        
        return response_content
    
    def clear_history(self):
        """
        Konuşma geçmişini temizler
        """
        self.conversation_history = []