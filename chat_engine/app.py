from flask import Flask, request, jsonify
from modular import NotificationEngine, InteractiveEngine

app = Flask(__name__)
notification_engine = NotificationEngine()
interactive_engine = InteractiveEngine()

@app.route('/chat', methods=['POST'])
def process_chat():
    
    if not request.is_json:
        return jsonify({"error": "Request must be JSON"}), 400
    
    data = request.get_json()
    user_data = data.get('user_data', '')
    engine_type = data.get('engine_type', 0)
    user_input = data.get('user_input', '')
    
    if not user_data and user_input:
        return jsonify({"error": "user_input and user_data is required"}), 400
    
    try:
        if engine_type == 0:
            response = notification_engine.process_query(user_input, user_data)
            return jsonify({
                "response": response
            })
        elif engine_type == 1:
            response = interactive_engine.process_query(user_input, user_data)
            return jsonify({
                "response": response
            })
        else:
            return jsonify({"error": "Invalid engine_type, must be 0 or 1"}), 400
    except Exception as e:
        return jsonify({"error": str(e)}), 500

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000, debug=False)
