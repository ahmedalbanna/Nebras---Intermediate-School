package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.viewmodel.MainViewModel
import com.example.ui.viewmodel.UiState
import kotlinx.coroutines.delay

data class VideoConcept(
    val id: String,
    val title: String,
    val subject: String,
    val description: String,
    val durationSeconds: Int,
    val stages: List<String>,
    val stageDetails: List<String>
)

data class RealWorldExampleItem(
    val title: String,
    val category: String, // "علمي" or "أدبي"
    val concept: String,
    val dailyStory: String,
    val explanation: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InteractiveLabScreen(
    viewModel: MainViewModel,
    onBack: () -> Unit
) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabTitles = listOf("مقاطع فيديو تعليمية 🎥", "محاكي الرسوم البيانية 📈", "تطبيقات الحياة اليومية 🌍")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("المختبر والوسائط التفاعلية 🎥🔬", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "رجوع")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            TabRow(selectedTabIndex = selectedTabIndex) {
                tabTitles.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = {
                            Text(
                                text = title,
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = if (selectedTabIndex == index) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    )
                }
            }

            when (selectedTabIndex) {
                0 -> ShortVideoConceptsSection()
                1 -> InteractiveDiagramSimulationsSection()
                2 -> RealWorldApplicationsSection(viewModel = viewModel)
            }
        }
    }
}

// -----------------------------------------------------------------------------
// SECTION 1: SHORT VIDEO CONCEPTS & STEP-BY-STEP ANIMATIONS
// -----------------------------------------------------------------------------
@Composable
fun ShortVideoConceptsSection() {
    val concepts = remember {
        listOf(
            VideoConcept(
                id = "mitosis",
                title = "الانقسام الميتوزي (الغير مباشر) 🧫",
                subject = "أحياء / علوم",
                description = "شرح مرئي لأطوار الانقسام الخلوي الأربعة وتضاعف المادة الوراثية لتكوين خليتين مماثلتين.",
                durationSeconds = 20,
                stages = listOf(
                    "1. الطور التمهيدي (Prophase)",
                    "2. الطور الاستوائي (Metaphase)",
                    "3. الطور الانفصالي (Anaphase)",
                    "4. الطور النهائي (Telophase)"
                ),
                stageDetails = listOf(
                    "تتثكف الشبكة الكروماتينية وتظهر الكروموسومات وتختفي النواة وتتكون خيوط المغزل.",
                    "تترتب الكروموسومات في خط استواء الخلية وتتصل بخيوط المغزل عند السنترومير.",
                    "ينقسم السنترومير وتنفصل كروماتيدات كل كروموسوم وتتجه لنصفي الخلية.",
                    "يتكون غشاء نووي جديد حول كل مجموعة وتتكون خليتان جديدتان مطابقتان للخلية الأصلية."
                )
            ),
            VideoConcept(
                id = "newton2",
                title = "قانون نيوتن الثاني للحركة (F = m × a) 🚀",
                subject = "فيزياء",
                description = "شرح مرئي لكيفية ارتباط القوة المحصلة بكتلة الجسم والتسارع الناتج.",
                durationSeconds = 15,
                stages = listOf(
                    "1. حالة السكون بدون قوة (F = 0)",
                    "2. تطبيق قوة صغيرة (F صغرى)",
                    "3. مضاعفة القوة (F مضاعفة)",
                    "4. زيادة كتلة الجسم (m أكبر)"
                ),
                stageDetails = listOf(
                    "تبقى الكتلة ثابتة ولا يحدث أي تسارع لأن القوة المحصلة تساوي صفراً.",
                    "يتكتسب الجسم تسارعاً متناسباً طردياً مع القوة المطبقة باتجاهها.",
                    "تتضاعف العجلة والتسارع فوراً مع زيادة القوة تؤكد العلاقة F = m × a.",
                    "تقل العجلة والتسارع مع زيادة كتلة الجسم عند ثبات القوة المؤثرة."
                )
            ),
            VideoConcept(
                id = "circulation",
                title = "الدورة الدموية الصغرى والكبرى 🫀",
                subject = "علوم / أحياء",
                description = "مسار حركة الدم من القلب للرئتين للتزود بالأكسجين ثم توزيعه لكافة خلايا الجسم.",
                durationSeconds = 18,
                stages = listOf(
                    "1. استقبال الدم غير المؤكسج",
                    "2. الدورة الدموية الصغرى (الرئوية)",
                    "3. عودة الدم المؤكسج للقلب",
                    "4. الدورة الدموية الكبرى (الجهازية)"
                ),
                stageDetails = listOf(
                    "يصل الدم المحمل بـ CO2 من أجزاء الجسم للأنين الأيمن ثم البطين الأيمن.",
                    "يُضخ الدم عبر الشريان الرئوي للرئتين لتفرغ CO2 والتزود بالأكسجين O2.",
                    "يعود الدم النقي والغني بالأكسجين للأنين الأيسر عبر الأوردة الرئوية.",
                    "يُضخ الدم بقوة عبر الشريان الأورطي لكافة أنحاء وأجهزة الجسم لتغذيتها."
                )
            )
        )
    }

    var selectedConcept by remember { mutableStateOf(concepts.first()) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        item {
            Text(
                text = "اختر المفهوم العلمي للمشاهدة والشرح المرئي 🎥:",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(10.dp))

            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(concepts) { concept ->
                    val isSelected = concept.id == selectedConcept.id
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier.clickable { selectedConcept = concept }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Videocam,
                                contentDescription = null,
                                tint = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = concept.title,
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            VideoPlayerSimulatorCard(concept = selectedConcept)
        }
    }
}

@Composable
fun VideoPlayerSimulatorCard(concept: VideoConcept) {
    var isPlaying by remember { mutableStateOf(false) }
    var currentProgressSeconds by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(isPlaying, concept.id) {
        if (isPlaying) {
            while (currentProgressSeconds < concept.durationSeconds) {
                delay(200)
                currentProgressSeconds += 0.2f
            }
            isPlaying = false
        }
    }

    val currentStageIndex = remember(currentProgressSeconds, concept) {
        val step = concept.durationSeconds.toFloat() / concept.stages.size
        (currentProgressSeconds / step).toInt().coerceIn(0, concept.stages.size - 1)
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header Info
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = concept.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.primaryContainer
                ) {
                    Text(
                        text = concept.subject,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(text = concept.description, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)

            Spacer(modifier = Modifier.height(16.dp))

            // Animated Visual Screen Box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFF0F172A)),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(16.dp)
                ) {
                    Surface(
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                        modifier = Modifier.size(54.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.Science,
                                contentDescription = null,
                                tint = Color(0xFF38BDF8),
                                modifier = Modifier.size(32.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = concept.stages[currentStageIndex],
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = concept.stageDetails[currentStageIndex],
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF94A3B8),
                        modifier = Modifier.padding(horizontal = 12.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Player Progress Bar & Time
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "00:${currentProgressSeconds.toInt().toString().padStart(2, '0')}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Slider(
                    value = currentProgressSeconds,
                    onValueChange = {
                        currentProgressSeconds = it
                        isPlaying = false
                    },
                    valueRange = 0f..concept.durationSeconds.toFloat(),
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 8.dp)
                )

                Text(
                    text = "00:${concept.durationSeconds.toString().padStart(2, '0')}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = {
                    currentProgressSeconds = 0f
                    isPlaying = true
                }) {
                    Icon(Icons.Default.Replay, contentDescription = "إعادة")
                }

                Spacer(modifier = Modifier.width(12.dp))

                Button(
                    onClick = { isPlaying = !isPlaying },
                    shape = CircleShape,
                    modifier = Modifier.size(50.dp),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Icon(
                        imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = "تشغيل/إيقاف"
                    )
                }
            }
        }
    }
}

// -----------------------------------------------------------------------------
// SECTION 2: INTERACTIVE DIAGRAMS & SIMULATIONS (OHM'S LAW, NEWTON'S 2ND, DENSITY)
// -----------------------------------------------------------------------------
@Composable
fun InteractiveDiagramSimulationsSection() {
    var selectedSim by remember { mutableIntStateOf(0) }
    val simTitles = listOf("⚡ قانون أوم (V=I×R)", "🚀 قانون نيوتن الثاني", "🧪 الكثافة والطفو")

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        item {
            Text(
                text = "اختر التجربة التفاعلية للتحكم بالمتغيرات والرسم البياني 📈:",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(10.dp))

            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(simTitles.indices.toList()) { index ->
                    val isSelected = selectedSim == index
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (isSelected) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier.clickable { selectedSim = index }
                    ) {
                        Text(
                            text = simTitles[index],
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            when (selectedSim) {
                0 -> OhmsLawSimulatorCard()
                1 -> NewtonsLawSimulatorCard()
                2 -> DensitySimulatorCard()
            }
        }
    }
}

@Composable
fun OhmsLawSimulatorCard() {
    var voltage by remember { mutableFloatStateOf(12f) } // V (Volts)
    var resistance by remember { mutableFloatStateOf(4f) } // R (Ohms)

    val current = if (resistance > 0) voltage / resistance else 0f // I = V / R

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "محاكي قانون أوم للعلاقة بين الجهد والتيار والمقاومة ⚡",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "القانون: V = I × R  ⬅️  التيار (I) = الجهد (V) ÷ المقاومة (R)",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Live Calculation Values
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ValueCardBox(label = "الجهد (V)", value = "${voltage.toInt()} فولت", color = Color(0xFFEAB308))
                ValueCardBox(label = "المقاومة (R)", value = "${resistance.toInt()} أوم", color = Color(0xFFEF4444))
                ValueCardBox(label = "التيار الناتج (I)", value = "${String.format("%.2f", current)} أمبير", color = Color(0xFF3B82F6))
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Interactive Sliders
            Text("تغيير الجهد الكهربائي (V): ${voltage.toInt()} V", style = MaterialTheme.typography.labelMedium)
            Slider(
                value = voltage,
                onValueChange = { voltage = it },
                valueRange = 1f..24f
            )

            Text("تغيير المقاومة الكهربائية (R): ${resistance.toInt()} Ω", style = MaterialTheme.typography.labelMedium)
            Slider(
                value = resistance,
                onValueChange = { resistance = it },
                valueRange = 1f..20f
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Dynamic Canvas Graph Representation
            Text("الرسم البياني للتيار الكهربائي محاطاً بجهد المصدر 📈:", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(6.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFF1E293B)),
                contentAlignment = Alignment.Center
            ) {
                val bulbGlowAlpha = (current / 10f).coerceIn(0.1f, 1f)
                val primaryColor = MaterialTheme.colorScheme.primary

                Canvas(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                    val w = size.width
                    val h = size.height

                    // Draw axis
                    drawLine(Color(0xFF64748B), Offset(0f, h), Offset(w, h), strokeWidth = 2f)
                    drawLine(Color(0xFF64748B), Offset(0f, 0f), Offset(0f, h), strokeWidth = 2f)

                    // Draw line representing I vs V
                    val currentH = (current / 12f) * h
                    drawLine(
                        color = Color(0xFF38BDF8),
                        start = Offset(0f, h),
                        end = Offset(w, (h - currentH).coerceAtLeast(0f)),
                        strokeWidth = 4f
                    )

                    // Draw glow bulb center
                    drawCircle(
                        color = Color(0xFFFACC15).copy(alpha = bulbGlowAlpha),
                        radius = 24.dp.toPx(),
                        center = Offset(w / 2, h / 2)
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Lightbulb,
                        contentDescription = null,
                        tint = Color(0xFFFACC15),
                        modifier = Modifier.size(36.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "شدة إضاءة اللمبة: ${(current * 10).toInt()}%",
                        color = Color.White,
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun NewtonsLawSimulatorCard() {
    var mass by remember { mutableFloatStateOf(10f) } // kg
    var force by remember { mutableFloatStateOf(50f) } // N

    val acceleration = if (mass > 0) force / mass else 0f // a = F / m

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "محاكي قانون نيوتن الثاني (F = m × a) 🚀",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "العجلة (التسارع) = القوة (F) ÷ الكتلة (m)",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ValueCardBox(label = "القوة (F)", value = "${force.toInt()} نيوتن", color = Color(0xFF10B981))
                ValueCardBox(label = "الكتلة (m)", value = "${mass.toInt()} كجم", color = Color(0xFF8B5CF6))
                ValueCardBox(label = "التسارع (a)", value = "${String.format("%.2f", acceleration)} م/ث²", color = Color(0xFFF59E0B))
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("القوة المؤثرة F: ${force.toInt()} N", style = MaterialTheme.typography.labelMedium)
            Slider(value = force, onValueChange = { force = it }, valueRange = 10f..200f)

            Text("كتلة الجسم m: ${mass.toInt()} kg", style = MaterialTheme.typography.labelMedium)
            Slider(value = mass, onValueChange = { mass = it }, valueRange = 1f..50f)

            Spacer(modifier = Modifier.height(12.dp))

            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Speed, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "النتيجة: كلما زادت القوة تزداد العجلة طردياً، وكلما زادت الكتلة يقل التسارع عكسياً.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}

@Composable
fun DensitySimulatorCard() {
    var mass by remember { mutableFloatStateOf(100f) } // g
    var volume by remember { mutableFloatStateOf(100f) } // cm³

    val density = if (volume > 0) mass / volume else 0f // d = m / V (Water density is 1.0 g/cm³)
    val floats = density <= 1.0f

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "محاكي الكثافة والطفو في الماء (d = m / V) 🧪",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "كثافة الماء النقية = 1.0 جرام/سم³. الأجسام الأقل كثافة تطفو، والأعلى تغوص.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ValueCardBox(label = "الكتلة (m)", value = "${mass.toInt()} جم", color = Color(0xFF6366F1))
                ValueCardBox(label = "الحجم (V)", value = "${volume.toInt()} سم³", color = Color(0xFF06B6D4))
                ValueCardBox(label = "الكثافة (d)", value = "${String.format("%.2f", density)} جم/سم³", color = if (floats) Color(0xFF16A34A) else Color(0xFFDC2626))
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("الكتلة m: ${mass.toInt()} جرام", style = MaterialTheme.typography.labelMedium)
            Slider(value = mass, onValueChange = { mass = it }, valueRange = 10f..300f)

            Text("الحجم V: ${volume.toInt()} سم³", style = MaterialTheme.typography.labelMedium)
            Slider(value = volume, onValueChange = { volume = it }, valueRange = 10f..300f)

            Spacer(modifier = Modifier.height(12.dp))

            Surface(
                shape = RoundedCornerShape(12.dp),
                color = if (floats) Color(0xFFDCFCE7) else Color(0xFFFEE2E2),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = if (floats) "🌊 حالة الجسم: يطفو فوق سطح الماء! (الكثافة ≤ 1.0)" else "⚓ حالة الجسم: يغوص في القاع! (الكثافة > 1.0)",
                    modifier = Modifier.padding(12.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = if (floats) Color(0xFF15803D) else Color(0xFF991B1B)
                )
            }
        }
    }
}

@Composable
fun ValueCardBox(label: String, value: String, color: Color) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = color.copy(alpha = 0.12f),
        border = androidx.compose.foundation.BorderStroke(1.dp, color.copy(alpha = 0.4f))
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(text = value, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold, color = color)
        }
    }
}

// -----------------------------------------------------------------------------
// SECTION 3: REAL-WORLD APPLICATIONS (SCIENTIFIC & LITERARY + AI GENERATOR)
// -----------------------------------------------------------------------------
@Composable
fun RealWorldApplicationsSection(viewModel: MainViewModel) {
    var conceptInput by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("علمي") }

    val realWorldState by viewModel.realWorldAppState.collectAsState()

    val preDesignedExamples = remember {
        listOf(
            RealWorldExampleItem(
                title = "قانون أوم والفيوزات الكهربائية في المنزل 🏠",
                category = "علمي",
                concept = "المقاومة والتيار والجهد (V = I × R)",
                dailyStory = "عند تشغيل المكيّف والغلاية الكهربائية والغسالة في وقت واحد، ينقطع القاطع أو ينصهر الفيوز فجأة!",
                explanation = "تزداد الأجهزة المتصلة على التوازي، فتقل المقاومة الكلية للشبكة المنزلية، مما يؤدي إلى تدفق تيار عالي جداً (I = V / R). ينصهر سلك الفيوز أوتوماتيكياً لحماية أسلاك المنزل من الاحتراق!"
            ),
            RealWorldExampleItem(
                title = "حزام الأمان والقصور الذاتي عند الفرملة 🚗",
                category = "علمي",
                concept = "قانون نيوتن الأول (القصور الذاتي)",
                dailyStory = "عندما يضغط سائق الحافلة على الفرامل فجأة، نندفع جميعاً للأمام تلقائياً!",
                explanation = "جسم الركب يحافظ على حالته الحركية بالسرعة السابقة للحافلة ويرفض التوقف الفجائي (القصور الذاتي)، لذا يمنعنا حزام الأمان من الاصطدام بالزجاج."
            ),
            RealWorldExampleItem(
                title = "التشبيه والاستعارة في الإعلانات الترويجية 📢",
                category = "أدبي",
                concept = "البلاغة والتشبيه البليغ الاستعاري",
                dailyStory = "نقرأ في إعلان مشروب طاقة: 'انطلق بأجنحة من ذهب'، أو في إعلان سيارة: 'وحش يروض الطريق'.",
                explanation = "يستخدم صانعو الإعلانات الاستعارة المكنية والتشبيه لإعطاء المنتج صفات الخفة أو القوة لإقناع المشتري عاطفياً وبصرياً."
            ),
            RealWorldExampleItem(
                title = "الطباق والمقابلة في المقالات الصحفية 📰",
                category = "أدبي",
                concept = "المحسنات البديعية (التضاد والمقابلة)",
                dailyStory = "يكتب الصحفي: 'العلم يرفع بيوتاً لا عماد لها، والجهل يهدم بيت العز والكرم'.",
                explanation = "المقابلة بين جملة البناء والرفع بعلم وجملة الهدم بالجهل تبرز المعنى وتوضحه بقوة في أذهان القراء لأن الأشياء بضدها تتبين."
            )
        )
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        // AI Generator Form for Real World Examples
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "مولد الأمثلة الواقعية بالذكاء الاصطناعي 🌍✨",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "اكتب أي مفهوم علمي أو أدبي لمعرفة كيف نراه ونعيشه في حياتنا اليومية",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf("علمي", "أدبي").forEach { cat ->
                            val isSel = selectedCategory == cat
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = if (isSel) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                                modifier = Modifier.clickable { selectedCategory = cat }
                            ) {
                                Text(
                                    text = cat,
                                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = if (isSel) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = conceptInput,
                        onValueChange = { conceptInput = it },
                        placeholder = { Text("مثال: الانكسار، الضغط الجوي، الطباق، التكثف...") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = { viewModel.generateRealWorldApplication(conceptInput, selectedCategory) },
                        enabled = conceptInput.isNotBlank(),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.AutoAwesome, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("ولّد التطبيق الواقعي 💡", fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        // Generated Result State
        item {
            when (val state = realWorldState) {
                is UiState.Success -> {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f))
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "التطبيق الواقعي للمفهوم 💡",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )

                                IconButton(onClick = {
                                    viewModel.saveResultToLibrary(
                                        title = "تطبيق واقعي: $conceptInput",
                                        subject = selectedCategory,
                                        itemType = "REAL_WORLD",
                                        inputContent = conceptInput,
                                        outputContent = state.data
                                    )
                                }) {
                                    Icon(Icons.Default.Save, contentDescription = "حفظ", tint = MaterialTheme.colorScheme.primary)
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))
                            Text(text = state.data, style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }

                is UiState.Loading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                else -> {}
            }
        }

        // Pre-designed Ready Examples List
        item {
            Text(
                text = "نماذج تطبيقات واقعية في الحياة اليومية 📚:",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(10.dp))
        }

        items(preDesignedExamples) { example ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = example.title,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )

                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = if (example.category == "علمي") MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.secondaryContainer
                        ) {
                            Text(
                                text = example.category,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "المفهوم: ${example.concept}",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "📌 الموقف في حياتنا: ${example.dailyStory}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(6.dp))
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "⚙️ التفسير: ${example.explanation}",
                            modifier = Modifier.padding(8.dp),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}
