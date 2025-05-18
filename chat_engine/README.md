# Chat Engine

Chat Engine, OpenAI sunucuları ile güvenli ve yönlendirilmiş iletişim kurmak amacıyla geliştirilmiş bir sunucu tarafı uygulamasıdır.

## 📡 Genel Bilgi

Chat Engine içerisindeki Flash tabanlı uygulama, **bir sunucu ortamında çalıştırılır**. Bu uygulama, mobil istemci ile OpenAI sunucuları arasındaki veri akışını yönlendirir.

## 📁 Yapılandırma Notları

- `chat_engine/` dizini altındaki dosyalar **mobil uygulamaya gömülmez**.
- Tüm işlemler sunucu tarafında gerçekleştirilir ve istemciler yalnızca bu ara katman üzerinden OpenAI API'sine erişir.
