# Kitapyurdu Test Otomasyon Projesi 🚀

Bu proje, **Kitapyurdu** e-ticaret web sitesinin temel fonksiyonlarını test etmek için geliştirilmiş kurumsal standartlarda bir **Test Otomasyon Framework'üdür**. 

Testler, sektörün en çok tercih edilen araçları olan **Selenium, Cucumber (BDD), TestNG, Maven ve Allure Raporlama** araçları kullanılarak Page Object Model (POM) tasarım desenine göre yazılmıştır.
Bu proje 212010020049 numaralı Zeynep Tuba Başar ve 212010020111 numaralı Dilanur Aygün tarafından yapılmıştır.
---

## 🛠️ Kullanılan Teknolojiler ve Mimari

*   **Java 21:** Projenin temel programlama dili.
*   **Selenium WebDriver (4.x):** Tarayıcıyı (Chrome) otomatik olarak kontrol etmek ve kullanıcı hareketlerini (tıklama, yazma, kaydırma) simüle etmek için kullanıldı.
*   **Cucumber (BDD):** Test senaryolarının İngilizce/Türkçe düz metin (*Given, When, Then*) formatında yazılmasını sağlayarak, kod bilmeyen iş analistlerinin veya yöneticilerin de testleri anlayabilmesine olanak tanır.
*   **TestNG:** Testlerin koşulmasını ve yönetilmesini sağlayan test motorudur.
*   **Maven:** Projedeki tüm kütüphanelerin (dependency) indirilmesini ve testlerin terminal üzerinden tek komutla çalıştırılmasını yönetir.
*   **Allure Report:** Test sonuçlarını, hataları ve ekran görüntülerini son derece şık ve detaylı bir web arayüzünde sunan görsel raporlama aracıdır.
*   **Page Object Model (POM):** Sitenin her sayfası (HomePage, BasketPage vb.) ayrı bir Java sınıfı (Class) olarak tasarlandı. Bu sayede kod tekrarı önlenmiş ve bakım kolaylığı sağlanmıştır.

---

## ✨ Projenin Öne Çıkan Özellikleri (Sunum Modu)

Bu proje hocanıza sunum yaparken etkileyici görünmesi için özel olarak ayarlandı:
1.  **Sıfır Bekleme:** Sitenin çerez (cookie) uyarıları özel CSS enjeksiyonu ile anında gizlenir, testler saniye kaybetmeden başlar.
2.  **Otomatik Kaydırma (Scroll):** Ekranda görünmeyen alanlara (örneğin sayfanın sonundaki butonlara) tıklanmadan önce sayfa otomatik olarak o bölgeye kayar.
3.  **Görsel Vurgulama (Highlighting):** Bir elemente tıklamadan veya yazı yazmadan önce, kod o alanı **kırmızı çerçeve ve sarı arka plan** ile işaretler ve izleyenlerin ne olduğunu anlaması için 1 saniye bekler.
4.  **Gerçek Bug Tespiti:** Sistemde gerçekten eksik olan bir Sınır Değer kuralını (çok uzun arama) yakalayan ve raporlayan özel bir senaryo (`BUG-01`) mevcuttur.

---

## 🏃‍♂️ Projeyi Çalıştırma ve Raporu Görüntüleme

Projeyi herhangi bir IDE'ye (IntelliJ, Eclipse) ihtiyaç duymadan doğrudan terminal üzerinden çalıştırabilirsiniz.

### Testleri Başlatmak İçin:
Terminalinizi (PowerShell / Command Prompt) projenin kök klasöründe açıp şu komutu yazın:
```bash
mvn clean test
```
*Bu komut önce eski sonuçları temizler, ardından testleri baştan sona çalıştırır.*

### Test Raporunu Görmek İçin:
Eğer `pom.xml` ayarlarınızdan dolayı test bittiğinde rapor otomatik açılmazsa, terminalinize şu komutu yapıştırarak görsel Allure Raporunu anında açabilirsiniz:
```bash
npx --yes allure-commandline serve allure-results
```

---

## 📝 Test Senaryoları ve Mantıksal Akışı

Testler tamamen gerçek bir kullanıcının siteye girip ürün aramasından sepetine ürün eklemesine kadar geçen **mantıksal bir sıraya** göre dizilmiştir.

### 1. Ana Sayfa ve Gezinme (Navigation)
*   **NAV-01:** Siteye giriş yapılır ve başlığın doğru olduğu doğrulanır.
*   **NAV-03:** Kategoriler menüsünden "Edebiyat" gibi spesifik bir kategoriye tıklanır ve sayfanın açıldığı onaylanır.

### 2. Arama İşlemleri (Search)
*   **NAV-02:** Roman, Biyografi gibi farklı kelimelerle normal aramalar yapılır. (Veri Güdümlü Test - Data Driven)
*   **NEG-01:** "xyzxyz123" gibi anlamsız bir metin aranır ve "Sonuç bulunamadı" mesajı beklenir.
*   **NEG-02:** Arama kutusu boş bırakılıp Enter'a basılır, sitenin çökmediği doğrulanır.
*   **BUG-01:** Arama kutusuna 300 harften oluşan çok uzun bir kelime gönderilir. Sitenin hata verip engellemesi beklenir ancak engellemediği için bu test bir "Sistem Açığı (Bug)" olarak Fail olur ve rapora eklenir.

### 3. Arama Sonuçları ve Filtreleme
*   **UI-01:** Arama sonuçlarındaki kitap kartlarında "Resim, İsim ve Fiyat" bilgilerinin eksiksiz yer aldığı doğrulanır.
*   **STOCK-01:** Stokta olan kitaplarda "Sepete Ekle" butonunun aktif olduğu görülür.
*   **SORT-01:** Ürünler "Ucuzdan Pahalıya" sıralanır ve sıralamanın doğru çalıştığı kontrol edilir.
*   **FILTER-01:** Sıralanan ürünlerin fiyatlarının geçerli rakamlar olduğu kontrol edilir.

### 4. Ürün Detay Sayfası
*   **DETAIL-01:** Bir kitaba tıklandığında ürün sayfasının tam yüklendiği ve isim/fiyat barındırdığı görülür.
*   **PRICE-01:** Ürün sayfasındaki fiyatın doğru gösterildiği doğrulanır.
*   **PRICE-02:** Arama sonuçlarından özellikle **İndirimli (Üstü çizili fiyata sahip)** bir ürün bulunur, sayfasına gidilir ve indirimli fiyatın orijinal fiyattan matematiksel olarak daha küçük olduğu ispatlanır.

### 5. Sepet İşlemleri (Cart)
*   **NEG-03:** "Tükendi" durumundaki kitapların sepet butonunun kapalı olduğu doğrulanır.
*   **CART-01:** İlk kitap sepete eklenir ve sepetteki ürün sayısının "1" olduğu doğrulanır.
*   **CART-03:** İkinci bir kitap daha eklenir ve sepet sayısının "2" olduğu görülür.
*   **CART-02:** Sepete gidilir ve sepetteki kitapların isimlerinin, arama sonuçlarındakilerle aynı olup olmadığı ve **Toplam Fiyatın**, sepetteki ürünlerin fiyatlarının toplamına eşit olup olmadığı detaylı bir şekilde doğrulanır.
