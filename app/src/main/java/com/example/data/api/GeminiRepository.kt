package com.example.data.api

import com.example.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GeminiRepository(private val apiService: GeminiApiService = RetrofitClient.service) {

    private fun getApiKey(): String {
        return BuildConfig.GEMINI_API_KEY.ifBlank { "" }
    }

    private val baseSystemInstruction = GeminiContent(
        parts = listOf(
            GeminiPart(
                text = """
                أنت معلم ومساعد تعليمي خبير متخصص في منهج المرحلة الإعدادية والمتوسطة (للطلاب في عمر 11-15 سنة).
                أسلوبك في الشرح:
                1. مبسط وواضح جداً مع استخدام أمثلة من الحياة اليومية وتشبيهات قريبة لذهن الطالب.
                2. التركيز التام على "فهم الطريقة والأساس العلمي" وليس مجرد إعطاء الإجابة النهائية.
                3. لغة عربية سليمة وودودة ومحفزة.
                4. تنصيق الإجابة بنقاط واضحة وعناوين بارزة.
                """.trimIndent()
            )
        )
    )

    suspend fun explainConcept(topic: String, subject: String, simplificationLevel: String = "عادي"): Result<String> =
        withContext(Dispatchers.IO) {
            try {
                val apiKey = getApiKey()
                if (apiKey.isEmpty() || apiKey == "MY_GEMINI_API_KEY") {
                    return@withContext Result.failure(Exception("يرجى إدخال مفتاح GEMINI_API_KEY في لوحة الأسرار (Secrets Panel) لاستخدام الذكاء الاصطناعي."))
                }

                val prompt = """
                المادة: $subject
                المفهوم المراد شرحه: $topic
                مستوى التبسيط: $simplificationLevel
                
                المطلوب:
                اشرح هذا المفهوم لطلاب المرحلة المتوسطة/الإعدادية بالهيكلية التالية:
                📌 **تعريف المفهوم بأسلوب مبسط**: (تعريف قصير وسهل الحفظ)
                💡 **التشبيه الذكي (مثال من واقع الحياة)**: (تشبيه يرسخ المفهوم)
                🔍 **النقاط والخصائص الرئيسية**: (3 إلى 4 نقاط)
                ❓ **سؤال سريع لاختبار فهمك**: (سؤال مباشر مع إجابته المخفية أو الموضحة بالأسفل)
                """.trimIndent()

                val request = GeminiRequest(
                    contents = listOf(GeminiContent(parts = listOf(GeminiPart(text = prompt)))),
                    systemInstruction = baseSystemInstruction
                )

                val response = apiService.generateContentFlash(apiKey, request)
                val text = response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
                    ?: "لم يتم استلام رد من النموذج."

                Result.success(text)
            } catch (e: Exception) {
                Result.failure(Exception("حدث خطأ أثناء الاتصال بالمعلم الذكي: ${e.localizedMessage}"))
            }
        }

    suspend fun solveMathPhysics(problemText: String, subject: String): Result<String> =
        withContext(Dispatchers.IO) {
            try {
                val apiKey = getApiKey()
                if (apiKey.isEmpty() || apiKey == "MY_GEMINI_API_KEY") {
                    return@withContext Result.failure(Exception("يرجى إدخال مفتاح GEMINI_API_KEY في لوحة الأسرار لاستخدام حلال المسائل الذكي."))
                }

                val prompt = """
                المادة: $subject (رياضيات / فيزياء / كيمياء للمرحلة المتوسطة)
                المسألة:
                "$problemText"
                
                الهدف الأساسي هو فهم الطريقة والأساس العلمي وليس الإجابة فقط!
                قم بالحل وفق الهيكلية التالية بالضبط:
                
                🎯 **فكرة المسألة والقانون المستعمل**:
                (اشرح الفكرة الفيزيائية/الرياضية والقوانين الرئيسية المستخدمة)
                
                📝 **معطيات ومطلوب المسألة**:
                (استخرج المعطيات والمطلوب بوضوح)
                
                🔢 **خطوات الحل التفصيلية (خطوة بخطوة)**:
                (اعرض كل خطوة مع التوضيح العلمي والتطبيق العددي)
                
                ✅ **النتيجة النهائية والوحدة**:
                
                ⚠️ **نصيحة لتجنب الخطأ الشائع في هذا النوع من المسائل**:
                """.trimIndent()

                val request = GeminiRequest(
                    contents = listOf(GeminiContent(parts = listOf(GeminiPart(text = prompt)))),
                    systemInstruction = baseSystemInstruction
                )

                // Using Pro model for complex reasoning in STEM
                val response = apiService.generateContentPro(apiKey, request)
                val text = response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
                    ?: "لم نتمكن من الحصول على تفاصيل الحل."

                Result.success(text)
            } catch (e: Exception) {
                Result.failure(Exception("خطأ أثناء حل المسألة: ${e.localizedMessage}"))
            }
        }

    suspend fun generateSimilarProblem(originalProblem: String, subject: String): Result<String> =
        withContext(Dispatchers.IO) {
            try {
                val apiKey = getApiKey()
                if (apiKey.isEmpty() || apiKey == "MY_GEMINI_API_KEY") {
                    return@withContext Result.failure(Exception("مفتاح API غير متوفر."))
                }

                val prompt = """
                بناءً على هذه المسألة في مادة $subject:
                "$originalProblem"
                
                قم بتوليد **مسألة جديدة تدريبية مشابهة لها تماماً بنفس الفكرة** ولكن بأرقام وسياق مختلف قليلاً ليتدرب عليها الطالب، ثم ضع الإجابة النهائية وخطوات الحل بشكل مخفي في الأسفل.
                """.trimIndent()

                val request = GeminiRequest(
                    contents = listOf(GeminiContent(parts = listOf(GeminiPart(text = prompt)))),
                    systemInstruction = baseSystemInstruction
                )

                val response = apiService.generateContentFlash(apiKey, request)
                val text = response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
                    ?: "عذراً، تعذر توليد المسألة التدريبية."

                Result.success(text)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    suspend fun summarizeLesson(lessonContent: String, subject: String): Result<String> =
        withContext(Dispatchers.IO) {
            try {
                val apiKey = getApiKey()
                if (apiKey.isEmpty() || apiKey == "MY_GEMINI_API_KEY") {
                    return@withContext Result.failure(Exception("يرجى إدخال مفتاح GEMINI_API_KEY في لوحة الأسرار."))
                }

                val prompt = """
                المادة: $subject
                نص الدرس المراد تلخيصه:
                "$lessonContent"
                
                قم بتلخيص هذا الدرس وتنسيقه للمذاكرة الذكية السريعة:
                
                📌 **العنوان والملخص العام**: (في سطرين)
                🔑 **المصطلحات والمفاهيم الرئيسية**: (تعريف كل مصطلح هام)
                ⭐ **أهم النقاط والمعلومات الأكاديمية**: (مرتبة بنقاط ورصاصات منظمة)
                🎴 **بطاقات مراجعة سريعة (سؤال وجواب)**:
                - س: [سؤال] | ج: [جواب]
                - س: [سؤال] | ج: [جواب]
                - س: [سؤال] | ج: [جواب]
                """.trimIndent()

                val request = GeminiRequest(
                    contents = listOf(GeminiContent(parts = listOf(GeminiPart(text = prompt)))),
                    systemInstruction = baseSystemInstruction
                )

                val response = apiService.generateContentFlash(apiKey, request)
                val text = response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
                    ?: "عذراً، تعذر تلخيص الدرس."

                Result.success(text)
            } catch (e: Exception) {
                Result.failure(Exception("خطأ في تلخيص الدرس: ${e.localizedMessage}"))
            }
        }

    suspend fun correctEssay(essayText: String, topic: String): Result<String> =
        withContext(Dispatchers.IO) {
            try {
                val apiKey = getApiKey()
                if (apiKey.isEmpty() || apiKey == "MY_GEMINI_API_KEY") {
                    return@withContext Result.failure(Exception("يرجى إدخال مفتاح GEMINI_API_KEY."))
                }

                val prompt = """
                الموضوع: $topic
                النص المكتوب من الطالب:
                "$essayText"
                
                قم بتصحيح وتقييم موضوع التعبير للمرحلة المتوسطة:
                
                📊 **التقييم العام**: (النقاط القوية + التقدير العام)
                🛠️ **التصحيحات النحوية والإملائية**: (جدول أو قائمة: الكلمة الأصلية ⬅️ التصحيح ⬅️ السبب)
                ✨ **مفردات وتعبيرات بلاغية مقترحة لتحسين النص**: (اقتراحات كلمات مرادفة وجمل جمالية مناسبة للمرحلة)
                📜 **النسخة المحسنة النهائية**: (أعد كتابة التعبير بشكل مشرق وممتع احتفاظاً بفكرة الطالب)
                """.trimIndent()

                val request = GeminiRequest(
                    contents = listOf(GeminiContent(parts = listOf(GeminiPart(text = prompt)))),
                    systemInstruction = baseSystemInstruction
                )

                val response = apiService.generateContentFlash(apiKey, request)
                val text = response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
                    ?: "لم نتمكن من تصحيح النص."

                Result.success(text)
            } catch (e: Exception) {
                Result.failure(Exception("خطأ في تصحيح النص: ${e.localizedMessage}"))
            }
        }

    suspend fun generateQuiz(topic: String, subject: String): Result<String> =
        withContext(Dispatchers.IO) {
            try {
                val apiKey = getApiKey()
                if (apiKey.isEmpty() || apiKey == "MY_GEMINI_API_KEY") {
                    return@withContext Result.failure(Exception("يرجى إدخال مفتاح GEMINI_API_KEY."))
                }

                val prompt = """
                المادة: $subject
                الموضوع: $topic

                أنشئ اختباراً تفاعلياً قصيراً مكوناً من 5 أسئلة اختيار من متعدد ملائمة لطلاب المرحلة الإعدادية والمتوسطة (11-15 سنة).
                يجب أن توضح التغذية الراجعة سبب صحة الإجابة وتشرح الخلط أو الأخطاء الشائعة لدى الطلاب.

                ارجع النتيجة حصراً بصيغة JSON داخل علامات ```json ... ``` بالشكل التالي دون أي نص خارجها:
                [
                  {
                    "question": "نص السؤال الأول هنا...",
                    "options": [
                      "الخيار الأول",
                      "الخيار الثاني",
                      "الخيار الثالث",
                      "الخيار الرابع"
                    ],
                    "correctAnswerIndex": 0,
                    "explanation": "التغذية الراجعة والتفسير العلمي: يوضح سبب صحة الإجابة وبيان الخطأ الشائع الذي يقع فيه الطلاب عادة."
                  }
                ]
                """.trimIndent()

                val request = GeminiRequest(
                    contents = listOf(GeminiContent(parts = listOf(GeminiPart(text = prompt)))),
                    systemInstruction = baseSystemInstruction
                )

                val response = apiService.generateContentFlash(apiKey, request)
                val text = response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
                    ?: "عذراً، تعذر إنشاء الاختبار."

                Result.success(text)
            } catch (e: Exception) {
                Result.failure(Exception("خطأ أثناء إنشاء الاختبار: ${e.localizedMessage}"))
            }
        }

    suspend fun generateStudyPlan(
        subjects: List<String>,
        currentLevel: String,
        learningPace: String,
        dailyHours: String,
        weakTopics: String
    ): Result<String> = withContext(Dispatchers.IO) {
        try {
            val apiKey = getApiKey()
            if (apiKey.isEmpty() || apiKey == "MY_GEMINI_API_KEY") {
                return@withContext Result.failure(Exception("يرجى إدخال مفتاح GEMINI_API_KEY لاستخدام مولد الخطط الدراسية."))
            }

            val subjectsStr = subjects.joinToString("، ")
            val prompt = """
            أنت خبير في التخطيط التربوي والجدولة الدراسية لطلاب المرحلة المتوسطة والإعدادية (11-15 سنة).
            قم بإنشاء جدول زمني مقترح وخطة دراسية شخصية مخصصة بناءً على معطيات الطالب التالية:
            - المواد المراد تحسين الأداء فيها: $subjectsStr
            - المستوى الحالي للطالب: $currentLevel
            - سرعة التعلم المفضلة: $learningPace
            - الوقت اليومي المتاح: $dailyHours
            - النقاط الواجب التركيز عليها: ${weakTopics.ifBlank { "لا توجد ملاحظات خاصة" }}

            المطلوب:
            قم بتوليد خطة دراسية متكاملة لمدة 5 إلى 7 أيام تحتوي على وحدات دراسية (STUDY)، تمارين وتطبيقات (EXERCISE)، واختبارات تقييمية (QUIZ).

            ارجع النتيجة بصيغة JSON حصرية داخل علامات ```json ... ``` بالشكل التالي دون أي نص خارجي لكي نتمكن من عرضها وتعديلها تفاعلياً في تطبيق الطالب:
            [
              {
                "dayName": "اليوم 1",
                "subject": "اسم المادة",
                "title": "عنوان الوحدة الدراسية أو الدرس",
                "taskType": "STUDY",
                "durationMinutes": 30,
                "description": "تفاصيل ما سيتم مذاكرته والنصيحة الدراسية"
              },
              {
                "dayName": "اليوم 1",
                "subject": "اسم المادة",
                "title": "عنوان التمارين",
                "taskType": "EXERCISE",
                "durationMinutes": 20,
                "description": "حل المسائل والتطبيقات العملية"
              },
              {
                "dayName": "اليوم 2",
                "subject": "اسم المادة",
                "title": "اختبار فهم سريع",
                "taskType": "QUIZ",
                "durationMinutes": 15,
                "description": "اختبار تقييمي للتحقق من الاستيعاب"
              }
            ]

            ملاحظة هامة: يجب أن تكون قيم "taskType" حصراً واحدة من: "STUDY" أو "EXERCISE" أو "QUIZ".
            """.trimIndent()

            val request = GeminiRequest(
                contents = listOf(GeminiContent(parts = listOf(GeminiPart(text = prompt)))),
                systemInstruction = baseSystemInstruction
            )

            val response = apiService.generateContentFlash(apiKey, request)
            val text = response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
                ?: "عذراً، تعذر إنشاء الخطة الدراسية."

            Result.success(text)
        } catch (e: Exception) {
            Result.failure(Exception("خطأ أثناء إنشاء الخطة الدراسية: ${e.localizedMessage}"))
        }
    }

    suspend fun generateRealWorldApplication(
        concept: String,
        category: String // "علمي" أو "أدبي"
    ): Result<String> = withContext(Dispatchers.IO) {
        try {
            val apiKey = getApiKey()
            if (apiKey.isEmpty() || apiKey == "MY_GEMINI_API_KEY") {
                return@withContext Result.failure(Exception("يرجى إدخال مفتاح GEMINI_API_KEY لاستخدام مولد الأمثلة الواقعية."))
            }

            val prompt = """
            أنت معلم مبتكر ومستشار تعليمي لطلاب المرحلة المتوسطة والإعدادية (11-15 سنة).
            قم بتقديم شرح تفاعلي ومثالي يربط المفهوم التالي ($concept) بتطبيق واقعي ملموس من الحياة اليومية لطلاب المدرسة.
            نوع المفهوم: $category

            نسق الإجابة بتصميم أنيق ومقسّم إلى النحو التالي:
            1. 📌 **تعريف المفهوم بأسلوب مبسط جداً**:
            [شرح المفهوم في سطرين فقط]

            2. 🌍 **كيف نراه ونعيشه في حياتنا اليومية؟**:
            [قصة قصيرة أو تطبيق ملموس من البيت، المدرسة، الشارع، التكنولوجيا، الإعلانات، أو الطبيعة]

            3. ⚙️ **التفسير العلمي / البلاغي للتطبيق**:
            [ربط المفهوم بالظاهرة الواقعية خطوة بخطوة]

            4. 💡 **سؤال تفكير ناقد ذكي لترسيخ الفهم**:
            [سؤال ممتع يربط المفهوم بموقف آخر في حياة الطالب]
            """.trimIndent()

            val request = GeminiRequest(
                contents = listOf(GeminiContent(parts = listOf(GeminiPart(text = prompt)))),
                systemInstruction = baseSystemInstruction
            )

            val response = apiService.generateContentFlash(apiKey, request)
            val text = response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
                ?: "عذراً، تعذر توليد التطبيق الواقعي."

            Result.success(text)
        } catch (e: Exception) {
            Result.failure(Exception("خطأ أثناء توليد التطبيق الواقعي: ${e.localizedMessage}"))
        }
    }
}
