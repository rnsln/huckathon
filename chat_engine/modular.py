class NotificationEngine():
    def __init__(self):
        import chains as cb
        import openai_keys as gk
        
        self.api_key = gk.openai_key
        self.qa_chain = cb.notification_chain(self.api_key)
    
    def process_query(self, user_input, user_data):
        """
        Process a user query without username information
        
        Args:
            user_input (str): User's message
            
        Returns:
            tuple: (response_content, None)
        """
        result = self.qa_chain.chat_with_qa(user_input, user_data)
        return result.content
    
class InteractiveEngine():
    def __init__(self):
        import chains as cb
        import openai_keys as gk
        
        self.api_key = gk.openai_key
        self.qa_chain = cb.interactive_chain(self.api_key)
    
    def process_query(self, user_input, user_data):
        """
        Process a user query with username information
        
        Args:
            user_input (str): User's message
            user_data (str): User's data
            
        Returns:
            tuple: (response_content, None)
        """
        result = self.qa_chain.chat_with_qa(user_input, user_data)
        return result