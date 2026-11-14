package dev.yanki.app

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.io.File

private val Lime=Color(0xFFC8FC26)
private val Blue=Color(0xFF283CEB)
private val Ink=Color(0xFF161A1E)
private val Muted=Color(0xFF737980)
private val Paper=Color(0xFFF8F8F5)
private val Gray=Color(0xFFEDF0EB)
private val Round=RoundedCornerShape(18.dp)
private data class Mission(val title:String,val place:String,val prompt:String,val example:String)
private val missions=listOf(
 Mission("Kafede sipariş verme","Kafe","Hi! What would you like to order?","I'd like a coffee, please."),
 Mission("Otelde rezervasyon","Otel","Hello, how can I help you?","I'd like to book a room for two nights."),
 Mission("Yol tarifi isteme","Şehir","Are you looking for something?","How can I get to the train station?"),
 Mission("Yeni biriyle tanış","Sosyal","Hello! What's your name?","My name is Alex. Nice to meet you.")
)
class MainActivity:ComponentActivity(){
 private var recorder:MediaRecorder?=null
 private var player:MediaPlayer?=null
 private fun record(slot:Int):Boolean {
  stopRecording()
  val file=File(filesDir,"attempt_$slot.m4a")
  return try {
   @Suppress("DEPRECATION")
   val r=if(Build.VERSION.SDK_INT>=31) MediaRecorder(this) else MediaRecorder()
   r.setAudioSource(MediaRecorder.AudioSource.MIC)
   r.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
   r.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
   r.setAudioSamplingRate(44100)
   r.setAudioEncodingBitRate(64000)
   r.setOutputFile(file.absolutePath)
   r.prepare()
   r.start()
   recorder=r
   true
  }catch(_:Exception){recorder?.release();recorder=null;false}
 }
 private fun stopRecording(){
  recorder?.let { try{it.stop()}catch(_:Exception){};it.release() }
  recorder=null
 }
 private fun listen(slot:Int) {
  player?.release();player=null
  val file=File(filesDir,"attempt_$slot.m4a")
  if(!file.exists())return
  try { player=MediaPlayer().apply{setDataSource(file.absolutePath);prepare();start()} }catch(_:Exception){}
 }
 override fun onDestroy(){stopRecording();player?.release();super.onDestroy()}
 override fun onCreate(savedInstanceState:Bundle?){
  super.onCreate(savedInstanceState)
  setContent {
   val ctx=LocalContext.current
   val prefs=remember{getSharedPreferences("yanki",MODE_PRIVATE)}
   var screen by remember{mutableStateOf(if(prefs.getBoolean("setup",false))"home" else "welcome")}
   var language by remember{mutableStateOf(prefs.getString("language","İngilizce")?:"İngilizce")}
   var goal by remember{mutableStateOf(prefs.getString("goal","Akıcı konuşmak")?:"Akıcı konuşmak")}
   var quizAnswer by remember{mutableStateOf("")}
   var selectedMission by remember{mutableIntStateOf(0)}
   var attempt by remember{mutableIntStateOf(1)}
   var recordedFirst by remember{mutableStateOf(File(filesDir,"attempt_1.m4a").exists())}
   var recordedSecond by remember{mutableStateOf(File(filesDir,"attempt_2.m4a").exists())}
   var recording by remember{mutableStateOf(false)}
   var recordMessage by remember{mutableStateOf("")}
   var completed by remember{mutableIntStateOf(prefs.getInt("completed",0))}
   var pair by remember{mutableStateOf("")}
   val micPermission=rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()){allowed->
    if(allowed){recording=record(attempt);recordMessage=if(recording)"" else "Mikrofon başlatılamadı"}
    else recordMessage="Mikrofon izni verilmedi"
   }
   val toggleRecord={
    if(recording){
     stopRecording();recording=false
     if(attempt==1) recordedFirst=File(filesDir,"attempt_1.m4a").exists()
     else {
      recordedSecond=File(filesDir,"attempt_2.m4a").exists()
      if(recordedSecond){completed++;prefs.edit().putInt("completed",completed).apply()}
     }
    }else{
     if(checkSelfPermission(Manifest.permission.RECORD_AUDIO)==PackageManager.PERMISSION_GRANTED){
      recording=record(attempt);recordMessage=if(recording)"" else "Kayıt başlatılamadı"
     }else micPermission.launch(Manifest.permission.RECORD_AUDIO)
    }
   }
   val navigate:(String)->Unit={dest->if(recording){stopRecording();recording=false};screen=dest}
   MaterialTheme(colorScheme=lightColorScheme(primary=Blue,secondary=Lime,background=Paper,surface=Color.White,onSurface=Ink)){
    Column(Modifier.fillMaxSize().background(Paper)){
     if(screen !in listOf("welcome","setup","quiz")){
      Row(Modifier.fillMaxWidth().padding(horizontal=20.dp, vertical=10.dp),verticalAlignment=Alignment.CenterVertically){
       Wave(Modifier.width(28.dp),Lime)
       Spacer(Modifier.width(8.dp))
       Text("YANKI",fontWeight=FontWeight.Black,fontSize=23.sp,modifier=Modifier.weight(1f))
       Text("●",color=Lime)
       Text(" Yerel",fontSize=12.sp,color=Muted)
      }
     }
     Box(Modifier.weight(1f)){
      when(screen){
       "welcome"->Welcome{navigate("setup")}
       "setup"->Body{
        Heading("Dil ve hedef","Öğrenme yolculuğunu ayarla.")
        Text("Hangi dili öğrenmek istiyorsun?",fontWeight=FontWeight.Bold)
        listOf("İngilizce","Almanca","Fransızca","İtalyanca").forEach{item->
         FilterChip(selected=language==item,onClick={language=item;prefs.edit().putString("language",item).apply()},label={Text(item)},modifier=Modifier.fillMaxWidth())
        }
        Spacer(Modifier.height(16.dp))
        Text("Hedefin ne?",fontWeight=FontWeight.Bold)
        listOf("Akıcı konuşmak","İş ve kariyer","Seyahat ve günlük yaşam","Sınav hazırlığı").forEach{item->
         FilterChip(selected=goal==item,onClick={goal=item;prefs.edit().putString("goal",item).apply()},label={Text(item)},modifier=Modifier.fillMaxWidth())
        }
        Text("İlk sürümün konuşma içerikleri İngilizcedir; diğer diller hazırlanıyor.",color=Muted,fontSize=12.sp)
        Action("Devam et"){navigate("quiz")}
       }
       "quiz"->Body{
        Heading("Hızlı seviye tespiti","Bu kısa örnek gerçek bir CEFR ölçümü değildir.")
        CardBox{
         Text("They ____ to the meeting yesterday.",fontSize=20.sp,fontWeight=FontWeight.Bold)
         listOf("go","went","gone","going").forEach{choice->
          OutlinedButton(onClick={quizAnswer=choice},modifier=Modifier.fillMaxWidth(),
           colors=ButtonDefaults.outlinedButtonColors(containerColor=if(quizAnswer==choice)Lime else Color.White)){
           Text(choice,color=Ink)
          }
         }
         if(quizAnswer.isNotBlank())Text(if(quizAnswer=="went")"Doğru!" else "Doğru cevap: went",color=Muted)
        }
        Action("Görevlere geç"){prefs.edit().putBoolean("setup",true).apply();navigate("home")}
       }
       "home"->Body{
        Heading("Merhaba, ilerlemeye hazır mısın?","Bugün de sesini duyur.")
        CardBox{
         Row(verticalAlignment=Alignment.CenterVertically){
          Column(Modifier.weight(1f)){
           Text("Tamamlanan görevler",color=Muted,fontSize=12.sp)
           Text("$completed",fontSize=32.sp,fontWeight=FontWeight.Black)
          }
          Box(Modifier.size(74.dp).clip(CircleShape).background(Lime),contentAlignment=Alignment.Center){Icon(Icons.Default.GraphicEq,null,Modifier.size(34.dp))}
         }
         Text("Yüzdelik bir akıcılık puanı değil, tamamladığın görevleri takip ediyoruz.",color=Muted,fontSize=12.sp)
        }
        Spacer(Modifier.height(18.dp))
        Heading("Bugün ne yapacaksın?")
        CardBox{
         Text("Kafede sipariş ver",fontSize=22.sp,fontWeight=FontWeight.Bold)
         Text("Gerçek hayat senaryosu • yaklaşık 3 dk",color=Muted)
         Wave(Modifier.fillMaxWidth(),Blue)
         Action("Senaryo görevi"){selectedMission=0;navigate("mission")}
        }
        Spacer(Modifier.height(10.dp))
        CardBox{
         Text("Rehberli konuşma",fontWeight=FontWeight.Bold)
         Text("Sesini kaydet, tekrar dinle, gelişimini gör.",color=Muted)
         Action("Konuşmaya başla",true){navigate("speaking")}
        }
        Spacer(Modifier.height(10.dp))
        CardBox{
         Text("Günlük meydan okuma",fontWeight=FontWeight.Bold)
         Text("ship / sheep: iki farklı sesi çalış.",color=Muted)
         Action("Meydan okumayı aç"){navigate("challenge")}
        }
       }
       "missions"->Body{
        Heading("Gerçek hayat senaryoları","Kısa, gerçekçi konuşmalarla pratik yap.")
        missions.forEachIndexed{index,m->
         CardBox{
          Text(m.place.uppercase(),color=Blue,fontSize=11.sp,fontWeight=FontWeight.Bold)
          Text(m.title,fontWeight=FontWeight.Bold,fontSize=20.sp)
          Text(m.prompt,color=Muted)
          Action("Görevi aç",true){selectedMission=index;navigate("mission")}
         }
         Spacer(Modifier.height(12.dp))
        }
       }
       "mission"->Body{
        val m=missions[selectedMission]
        Heading(m.title,"GERÇEK HAYAT SENARYOSU")
        CardBox{
         Box(Modifier.fillMaxWidth().height(145.dp).clip(Round).background(Gray),contentAlignment=Alignment.Center){
          Text(m.place,fontSize=30.sp,fontWeight=FontWeight.Bold)
         }
         Text("Karşındaki kişi:",color=Muted)
         Text(m.prompt,fontSize=22.sp,fontWeight=FontWeight.Bold)
        }
        Spacer(Modifier.height(12.dp))
        CardBox{
         Text("Görev akışı",fontWeight=FontWeight.Bold)
         Text("1 • Durumu dinle ve anla\n2 • Kendi diyaloğunu kaydet\n3 • Örnek ifade ve IPA'yı incele\n4 • İkinci kez konuş ve karşılaştır")
        }
        Spacer(Modifier.height(16.dp))
        Action("Göreve başla"){attempt=1;navigate("speaking")}
       }
       "speaking"->Body{
        val m=missions[selectedMission]
        Heading("Rehberli konuşma",m.title)
        CardBox{
         Text("Karşındaki kişi",color=Muted,fontSize=12.sp)
         Text(m.prompt,fontSize=20.sp,fontWeight=FontWeight.Bold)
         Wave(Modifier.fillMaxWidth(),Blue)
        }
        Spacer(Modifier.height(12.dp))
        CardBox{
         Text(if(attempt==1)"1. İlk denemeni kaydet" else "2. Yeni denemeni kaydet",fontSize=19.sp,fontWeight=FontWeight.Bold)
         Box(Modifier.fillMaxWidth().padding(18.dp),contentAlignment=Alignment.Center){
          FilledIconButton(onClick=toggleRecord,modifier=Modifier.size(86.dp),
           colors=IconButtonDefaults.filledIconButtonColors(containerColor=if(recording)Color(0xFFDC5757) else Blue)){
           Icon(if(recording)Icons.Default.Stop else Icons.Default.Mic,"Mikrofon",modifier=Modifier.size(38.dp))
          }
         }
         Text(if(recording)"Kaydediliyor. Durdurmak için dokun." else "Konuşmak için mikrofona dokun.",
          modifier=Modifier.fillMaxWidth(),textAlign=TextAlign.Center,color=Muted)
         if(recordMessage.isNotEmpty())Text(recordMessage,color=Color.Red,fontSize=12.sp)
         if(recordedFirst) OutlinedButton(onClick={listen(1)},modifier=Modifier.fillMaxWidth()){Text("İlk kaydı dinle")}
         if(recordedSecond) OutlinedButton(onClick={listen(2)},modifier=Modifier.fillMaxWidth()){Text("İkinci kaydı dinle")}
        }
        Spacer(Modifier.height(12.dp))
        if(recordedFirst&&attempt==1)CardBox{
         Text("Örnek ifade",fontWeight=FontWeight.Bold)
         Text(m.example,fontSize=18.sp)
         Text("Bu örnek senaryo metnidir, otomatik ses tanıma sonucu değildir.",color=Muted,fontSize=12.sp)
         Action("IPA'yı incele ve tekrar dene",true){attempt=2;navigate("ipa")}
        }
        if(recordedSecond)Action("İki kaydı karşılaştır"){navigate("analysis")}
       }
       "ipa"->Body{
        Heading("Telaffuz eğitmeni","IPA • Artikülasyon • Karşılaştırma")
        CardBox{
         Text("/θ/",fontSize=60.sp,fontWeight=FontWeight.Light,textAlign=TextAlign.Center,modifier=Modifier.fillMaxWidth())
         Text("think • three • nothing",modifier=Modifier.fillMaxWidth(),textAlign=TextAlign.Center)
         Text("Dil ucunu üst ve alt dişlerinin arasına hafifçe getir. Ses tellerini titreştirmeden hava akışını sürdür.")
         Text("Ses kaydı dilin fiziksel yerini tespit etmez. Bu açıklama eğitim amaçlıdır.",fontSize=12.sp,color=Muted)
        }
        Spacer(Modifier.height(12.dp))
        CardBox{
         Text("Örnek sözcükler",fontWeight=FontWeight.Bold)
         Text("think /θɪŋk/\nthree /θriː/\nnothing /ˈnʌθɪŋ/",lineHeight=27.sp)
         Text("Lisansı doğrulanmış ses örnekleri ve görseller daha sonra eklenecek.",color=Muted,fontSize=12.sp)
        }
        Spacer(Modifier.height(15.dp))
        Action("İkinci konuşma denemesi"){attempt=2;navigate("speaking")}
       }
       "analysis"->Body{
        Heading("Konuşma analizi","Önce ve sonra • Senin gerçek kayıtların")
        CardBox{
         Text("İlk kayıt",fontWeight=FontWeight.Bold)
         Wave(Modifier.fillMaxWidth(),Muted)
         OutlinedButton(onClick={listen(1)},enabled=recordedFirst,modifier=Modifier.fillMaxWidth()){Text("Önceki kaydı oynat")}
         HorizontalDivider()
         Text("İkinci kayıt",fontWeight=FontWeight.Bold)
         Wave(Modifier.fillMaxWidth(),Blue)
         OutlinedButton(onClick={listen(2)},enabled=recordedSecond,modifier=Modifier.fillMaxWidth()){Text("Sonraki kaydı oynat")}
        }
        Spacer(Modifier.height(12.dp))
        CardBox{
         Text("Değerlendirme",fontWeight=FontWeight.Bold)
         Text("Kayıt karşılaştırması kullanılabilir. Henüz doğrulanmış otomatik fonem veya akıcılık puanlaması yok.")
         Text("Gerçek ölçüm olmadan %84 gibi başarı yüzdeleri göstermiyoruz.",fontSize=12.sp,color=Muted)
        }
        Spacer(Modifier.height(12.dp))
        Action("Meydan okumayı paylaş"){navigate("share")}
       }
       "challenge"->Body{
        Heading("Günlük meydan okuma","Minimal çiftler • Fonetik farkındalık")
        CardBox{
         Text("ship  /ʃɪp/\nsheep  /ʃiːp/",fontSize=23.sp,fontWeight=FontWeight.Bold)
         Text("Bu sözcüklerdeki iki ünlü sesini karşılaştır.",color=Muted)
         Row(horizontalArrangement=Arrangement.spacedBy(8.dp)){
          listOf("ship","sheep").forEach{word->
           FilterChip(selected=pair==word,onClick={pair=word},label={Text(word)},modifier=Modifier.weight(1f))
          }
         }
         Text("Ses örnekleri eklenmeden doğru/yanlış puanı üretilmez.",fontSize=12.sp,color=Muted)
        }
        Spacer(Modifier.height(12.dp))
        Action("IPA eğitimine git"){navigate("ipa")}
       }
       "community"->Body{
        Heading("Topluluk","Arkadaşlarınla görev paylaş.")
        CardBox{
         Text("Birlikte öğren",fontSize=20.sp,fontWeight=FontWeight.Bold)
         Text("Meydan okumayı bir arkadaşına gönder. Gerçek zamanlı arkadaş listesi ve lig henüz aktif değil.",color=Muted)
         Action("Daveti paylaş"){navigate("share")}
        }
        Spacer(Modifier.height(12.dp))
        CardBox{
         Text("Ses meydan okuması",fontWeight=FontWeight.Bold)
         Text("Minimal çiftlerle dil becerilerini geliştir.")
         Action("Başlat",true){navigate("challenge")}
        }
       }
       "progress"->Body{
        Heading("İlerleme ve tekrar","Kendi kayıtlarını dinleyerek gelişimini izle.")
        CardBox{
         Text("Tamamlanan konuşma görevleri",color=Muted)
         Text("$completed",fontSize=46.sp,fontWeight=FontWeight.Black,color=Blue)
         Text("Takip gerçek tamamlanan ikinci denemeler üzerinden yapılır.")
        }
        Spacer(Modifier.height(12.dp))
        CardBox{
         Text("Önce / sonra kayıtları",fontWeight=FontWeight.Bold)
         Wave(Modifier.fillMaxWidth(),Blue)
         Action("Kayıtları dinle",true){navigate("analysis")}
        }
        Spacer(Modifier.height(12.dp))
        CardBox{
         Text("Tekrar planı",fontWeight=FontWeight.Bold)
         Text("Bir sonraki sürümde FSRS ile yerel aralıklı tekrar takvimi eklenecek.",color=Muted)
        }
       }
       "share"->Body{
        Heading("Paylaş","Öğrenme meydan okumanı arkadaşına gönder.")
        CardBox{
         Box(Modifier.fillMaxWidth().height(260.dp).clip(Round).background(Ink),contentAlignment=Alignment.Center){
          Column(horizontalAlignment=Alignment.CenterHorizontally){
           Text("YANKI",color=Lime,fontSize=30.sp,fontWeight=FontWeight.Black)
           Wave(Modifier.padding(horizontal=28.dp),Lime)
           Text("$completed konuşma görevi",color=Color.White,fontSize=21.sp)
           Text("Konuş • Öğren • Paylaş",color=Color.White)
          }
         }
         Text("Bu alan şimdilik paylaşım önizlemesidir. MP4 oluşturma henüz etkin değildir.",fontSize=12.sp,color=Muted)
         Action("Daveti gönder"){
          val send=Intent(Intent.ACTION_SEND).apply{
           type="text/plain"
           putExtra(Intent.EXTRA_TEXT,"YANKI ile İngilizce konuşma görevleri yapıyorum. Sen de deneyebilir misin? #YANKI")
          }
          ctx.startActivity(Intent.createChooser(send,"Paylaş"))
         }
        }
       }
       "profile"->Body{
        Heading("Profil ve ayarlar","YANKI • Yerel profil")
        CardBox{
         Text("Öğrenme dili: $language",fontWeight=FontWeight.Bold)
         Text("Hedef: $goal")
         Text("Tamamlanan görev: $completed")
        }
        Spacer(Modifier.height(12.dp))
        CardBox{
         Text("Veri ve gizlilik",fontWeight=FontWeight.Bold)
         Text("Ses kayıtların uygulamanın özel dizininde kalır. Ücretli API veya sunucuya ses aktarımı yok.",color=Muted)
         Text("Hesap, bulut senkronizasyonu ve sosyal sıralama daha sonra eklenecek.",color=Muted)
         Action("Hedefleri düzenle",true){navigate("setup")}
        }
       }
      }
     }
     if(screen !in listOf("welcome","setup","quiz")){
      NavigationBar(containerColor=Color.White){
       val labels=listOf("Ana Sayfa","Keşfet","Konuş","İlerleme","Profil")
       val routes=listOf("home","missions","speaking","progress","profile")
       val icons=listOf(Icons.Default.Home,Icons.Default.Search,Icons.Default.Mic,Icons.Default.BarChart,Icons.Default.Person)
       labels.forEachIndexed{index,label->
        NavigationBarItem(selected=screen==routes[index],onClick={navigate(routes[index])},
         icon={Icon(icons[index],label)},label={Text(label,fontSize=10.sp)},
         colors=NavigationBarItemDefaults.colors(selectedIconColor=Blue,indicatorColor=Lime))
       }
      }
     }
    }
   }
  }
 }
}
@Composable private fun Body(content:@Composable ColumnScope.()->Unit){
 Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(horizontal=18.dp, vertical=12.dp),content=content)
}
@Composable private fun Heading(title:String,subtitle:String=""){
 Text(title,fontSize=24.sp,fontWeight=FontWeight.ExtraBold,color=Ink)
 if(subtitle.isNotEmpty())Text(subtitle,fontSize=13.sp,color=Muted)
 Spacer(Modifier.height(15.dp))
}
@Composable private fun CardBox(content:@Composable ColumnScope.()->Unit){
 Column(Modifier.fillMaxWidth().clip(Round).background(Color.White).padding(16.dp),
  verticalArrangement=Arrangement.spacedBy(10.dp),content=content)
}
@Composable private fun Action(text:String,secondary:Boolean=false,onClick:()->Unit){
 Button(onClick=onClick,modifier=Modifier.fillMaxWidth().height(51.dp),shape=RoundedCornerShape(15.dp),
  colors=ButtonDefaults.buttonColors(containerColor=if(secondary)Lime else Blue,contentColor=if(secondary)Ink else Color.White)){
  Text(text,fontWeight=FontWeight.Bold)
 }
}
@Composable private fun Wave(modifier:Modifier=Modifier,tint:Color=Blue){
 Canvas(modifier.height(50.dp)){
  val mid=size.height/2f
  for(i in 0..54){
   val x=size.width*i/54f
   val h=(4+(i*37%23)*0.8f+(i*13%7)).coerceAtMost(37f)
   drawLine(tint,Offset(x,mid-h/2),Offset(x,mid+h/2),strokeWidth=2.5.dp.toPx(),cap=StrokeCap.Round)
  }
 }
}
@Composable private fun Welcome(next:()->Unit){
 Column(Modifier.fillMaxSize().padding(22.dp),horizontalAlignment=Alignment.CenterHorizontally,verticalArrangement=Arrangement.SpaceBetween){
  Spacer(Modifier.height(22.dp))
  Column(horizontalAlignment=Alignment.CenterHorizontally){
   Text("YANKI",fontSize=46.sp,fontWeight=FontWeight.Black,letterSpacing=3.sp)
   Text("DİL ÖĞRENMENİN DOĞAL YANKISI",fontSize=11.sp,letterSpacing=1.5.sp)
  }
  Box(Modifier.fillMaxWidth().height(250.dp).clip(RoundedCornerShape(30.dp)).background(Gray),contentAlignment=Alignment.Center){
   Column(horizontalAlignment=Alignment.CenterHorizontally){
    Box(Modifier.size(115.dp).clip(CircleShape).background(Lime))
    Spacer(Modifier.height(14.dp))
    Wave(Modifier.fillMaxWidth().padding(horizontal=34.dp),Blue)
   }
  }
  Column{
   Heading("Bir dili, gerçek konuşma deneyimiyle öğren.","Dinle, konuş, analiz et, tekrar et. Gelişimini kendi sesinden duy.")
   Action("Hadi başlayalım",true,next)
  }
 }
}
