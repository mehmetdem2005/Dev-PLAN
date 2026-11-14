package dev.yanki.app.data

data class Mission(
    val id: String,
    val title: String,
    val setting: String,
    val prompt: String,
    val translation: String,
    val hint: String,
    val level: String = "A2",
    val minutes: Int = 3,
    val category: String = "Günlük yaşam"
)
data class SoundLesson(
    val symbol: String,
    val name: String,
    val instruction: String,
    val example: String,
    val transcription: String,
    val contrast: String
)
data class PairLesson(val first: String, val second: String, val firstIpa: String, val secondIpa: String)

object LearningContent {
    val languages = listOf("İngilizce", "Almanca", "İtalyanca", "İspanyolca", "Fransızca")
    val goals = listOf("Akıcı konuşmak", "İş ve kariyer", "Seyahat etmek", "Günlük sohbet")
    val missions = listOf(
        Mission("cafe", "Kafede sipariş verme", "KAFE", "I'd like a cappuccino, please.", "Bir cappuccino rica ediyorum.", "Kibarca bir içecek iste.", category = "Günlük yaşam"),
        Mission("table", "Masa rezervasyonu", "RESTORAN", "I'd like to book a table for two.", "İki kişilik masa ayırtmak istiyorum.", "Kişi sayısını ve isteğini söyle."),
        Mission("station", "İstasyonda yön sorma", "İSTASYON", "Excuse me, where is platform three?", "Affedersiniz, üçüncü peron nerede?", "Önce dikkat çek, sonra yön sor."),
        Mission("hotel", "Otele giriş", "OTEL", "I have a reservation under my name.", "Adıma bir rezervasyonum var.", "Resepsiyona rezervasyonunu bildir."),
        Mission("job", "İş görüşmesinde tanışma", "OFİS", "I have experience in customer service.", "Müşteri hizmetlerinde tecrübem var.", "Deneyiminden bir cümleyle bahset.", level = "B1", category = "İş ve kariyer"),
        Mission("lost", "Kaybolunca yardım isteme", "ŞEHİR", "Could you show me the way to the museum?", "Müzeye giden yolu gösterebilir misiniz?", "Nazik bir şekilde yardım iste."),
        Mission("doctor", "Doktora durumu açıklama", "KLİNİK", "I've had a headache since yesterday.", "Dünden beri başım ağrıyor.", "Şikâyetinin süresini söyle.", level = "B1"),
        Mission("return", "Ürün iadesi", "MAĞAZA", "I'd like to return this item, please.", "Bu ürünü iade etmek istiyorum.", "Ne istediğini kibarca açıkla."),
        Mission("meeting", "Toplantı saati değiştirme", "OFİS", "Could we move the meeting to Friday?", "Toplantıyı cuma gününe alabilir miyiz?", "Yeni bir gün öner.", level = "B1", category = "İş ve kariyer"),
        Mission("friend", "Yeni biriyle tanışma", "PARK", "What do you like doing in your free time?", "Boş zamanlarında ne yapmayı seversin?", "Açık uçlu bir soru sor.")
    )
    val sounds = listOf(
        SoundLesson("/θ/", "Dişler arası ses", "Dil ucunu üst ve alt ön dişlerin arasına hafifçe yerleştir. Ses tellerini titreştirmeden hava ver.", "think", "/θɪŋk/", "/t/ ile karıştırma"),
        SoundLesson("/ð/", "Ötümlü dişler arası ses", "Dili hafifçe dişlerin arasına getir; hava akarken ses tellerini titreştir.", "this", "/ðɪs/", "/d/ ile karıştırma"),
        SoundLesson("/ɪ/", "Kısa gevşek ünlü", "Dili öne ve yukarı yakın tut. Dudakları gevşet; sesi kısa üret.", "ship", "/ʃɪp/", "/iː/ ile karşılaştır"),
        SoundLesson("/iː/", "Uzun ön ünlü", "Dili yükseğe ve öne taşı, dudaklar yaygın. Sesi uzun tut.", "sheep", "/ʃiːp/", "/ɪ/ ile karşılaştır"),
        SoundLesson("/æ/", "Açık ön ünlü", "Çeneyi belirgin aç, dili önde ve aşağıda tut.", "cat", "/kæt/", "/e/ ile karıştırma"),
        SoundLesson("/w/", "Dudak yuvarlaması", "Dudakları kısa süre yuvarlayıp hemen sonraki ünlüye geç.", "we", "/wiː/", "/v/ ile karıştırma")
    )
    val pairs = listOf(
        PairLesson("ship", "sheep", "/ʃɪp/", "/ʃiːp/"),
        PairLesson("live", "leave", "/lɪv/", "/liːv/"),
        PairLesson("think", "tink", "/θɪŋk/", "/tɪŋk/"),
        PairLesson("west", "vest", "/west/", "/vest/"),
        PairLesson("bad", "bed", "/bæd/", "/bed/")
    )
}
