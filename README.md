# E-Ticaret API Projesi

![Java CI with Maven](https://github.com/ayferdilay/api_project/actions/workflows/maven.yml/badge.svg)

## Proje Hakkında

Projede, basit bir e-ticaret sisteminin arka planını (backend) simüle eden bir REST API geliştirdim. Kullanıcılar üye olabiliyor, ürünleri listeleyebiliyor, sipariş verebiliyor ve ürünlere yorum yapabiliyor.

Amacım sadece çalışan bir kod yazmak değil, aynı zamanda bu kodun kalitesini testlerle güvence altına almaktı. Bu yüzden projede hem birim testlere (Unit Tests) hem de sistem testlerine (Integration/E2E) ağırlık verdim.

### Kullandığım Teknolojiler

Projeyi geliştirirken şu araçları ve kütüphaneleri kullandım:

- **Dil:** Java 21
- **Framework:** Spring Boot 3.4.1
- **Veritabanı:** H2 (Proje içinde gömülü çalışıyor, kurulum gerektirmiyor)
- **Test:** JUnit 5 ve Mockito
- **Dokümantasyon:** Swagger (OpenAPI)

## Nasıl Çalıştırılır?

Projeyi bilgisayarınıza indirip çalıştırmak için şu adımları izleyebilirsiniz:

1. Önce projeyi bilgisayarınıza indirin:

   ```bash
   git clone https://github.com/ayferdilay/api_project.git
   cd api-project
   ```

2. Terminalden şu komutu yazarak uygulamayı başlatın (Maven yüklü olmasa bile çalışır):

   ```bash
   ./mvnw spring-boot:run
   ```

3. Uygulama açıldığında `http://localhost:8080` adresinden erişebilirsiniz.

## API Dokümantasyonu

Projenin tüm API detaylarına (Endpoint'ler, parametreler, cevap tipleri) Swagger UI üzerinden erişebilirsiniz:

👉 **[Swagger UI'a Gitmek İçin Tıkla](http://localhost:8080/swagger-ui.html)**  
Link: `http://localhost:8080/swagger-ui.html`

### Endpoint Listesi

Projede bulunan temel servisler şunlardır:

| Metot  | Yol             | Açıklama                                    |
| :----- | :-------------- | :------------------------------------------ |
| `GET`  | `/api/users`    | Kayıtlı tüm kullanıcıları listeler.         |
| `POST` | `/api/users`    | Sisteme yeni bir kullanıcı kaydeder.        |
| `GET`  | `/api/products` | Satıştaki tüm ürünleri listeler.            |
| `POST` | `/api/orders`   | Kullanıcı adına yeni bir sipariş oluşturur. |
| `POST` | `/api/reviews`  | Bir ürüne yorum yapmayı sağlar.             |

### Kullanım Örneği: Yeni Kullanıcı Ekleme

Sisteme **POST** isteği atarak yeni kullanıcı oluşturabilirsiniz.

**URL:** `/api/users`  
**JSON Body:**

```json
{
  "name": "Örnek Kişi",
  "email": "kisi@mail.com",
  "password": "sifre123"
}
```

**Başarılı Cevap (201 Created):**

```json
{
  "id": 10,
  "name": "Örnek Kişi",
  "email": "kisi@mail.com",
  "password": "sifre123"
}
```

## Testler

Toplamda **65** test yazdım ve hepsi başarıyla çalışıyor.

- **Birim Testler (Service):** 31
- **Entegrasyon Testleri (Controller):** 23
- **Repository Testleri:** 6
- **Sistem/E2E Testleri:** 5

Testleri topluca çalıştırmak için terminale şunu yazmanız yeterli:

```bash
./mvnw test
```

### Neleri Test Ettim?

1. **Birim Testler (Unit Tests):** Servis katmanındaki metotları (User, Product, Order servisleri) dış bağımlılıkları mocklayarak test ettim. Is mantığını burada doğruladım.
2. **Entegrasyon Testleri:** Controller katmanını test ettim. İsteklerin doğru gidip gitmediğini ve veritabanı sorgularının doğru çalışıp çalışmadığını kontrol ettim.
3. **Sistem Testleri (E2E):** Bir kullanıcının siteye girip ürün alıp yorum yapması gibi uçtan uca senaryoları test ettim (SystemIntegrationTest).

## CI/CD (Otomatik Kontrol)

GitHub Actions kullanarak projeye otomatik test süreci ekledim. Her kod yüklediğimde (push) GitHub sunucularında testler otomatik olarak çalışıyor. Yukarıdaki "Java CI with Maven" alanından durumunu görebilirsiniz.
