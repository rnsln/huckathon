# 🔐 Luminance API – Chat Engine Arayüz Katmanı

Bu API, **Lümin kullanıcı verilerinin güvenli bir şekilde yönlendirilmesi** ve **chat_engine** ile olan iletişimin yönetilmesi amacıyla geliştirilmiştir.

---

## 🎯 Amaç

- Kullanıcının **akıllı cihazlarından (bileklik/saat)** gelen verileri işleyerek, chat_engine’e yönlendirmek.
- **Veri yorumlama** işlemlerinin sunucu tarafında yapılmasını sağlamak.
- **Mobil uygulama içerisinde veri saklanmaması**, uygulamanın:
  - Daha **hafif** çalışmasına,
  - Veri kaybı riskinin ortadan kalkmasına,
  - Kullanıcı bilgilerinin **güvenli bir şekilde sunucuda kalmasına** olanak tanır.

---

## 🔐 Güvenlik

- `chat_engine` sadece bu API aracılığıyla erişilebilir durumdadır.
- Bu yapı sayesinde dış kaynaklardan doğrudan erişim engellenir ve **yetkisiz veri akışları önlenmiş olur**.

---

## ⚙️ Çalışma Ortamı

- Bu API, **mobil cihazda çalışmaz**.
- Harici bir sunucu üzerinden çalıştırılır.
- Mobil uygulama, verileri bu API üzerinden chat_engine’e iletir ve geri dönüşleri alır.
