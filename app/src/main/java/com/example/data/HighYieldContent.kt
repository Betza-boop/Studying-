package com.example.data

data class Flashcard(
    val question: String,
    val answer: String
)

data class ThemeItem(
    val id: String,
    val title: String,
    val synthesis: String,
    val focusPoints: List<String>,
    val flashcards: List<Flashcard>
)

data class StudyBlock(
    val id: Int,
    val title: String,
    val priority: String,
    val hours: String,
    val description: String,
    val themes: List<ThemeItem>
)

object HighYieldContent {
    val blocks = listOf(
        StudyBlock(
            id = 1,
            title = "Bloque 1: Patología Celular y Tisular General",
            priority = "Máxima Priority (High-Yield)",
            hours = "12 Horas",
            description = "Estudio del fundamento de la lesión celular reversible e irreversible, acumulaciones intracelulares y calcificaciones patológicas.",
            themes = listOf(
                ThemeItem(
                    id = "t1",
                    title = "Tema 1: Métodos de Estudio y Muestras",
                    synthesis = "La Morfofisiopatología Humana estudia la etiopatogenia y alteraciones morfofuncionales. La muestra biológica es la porción de tejido o fluido obtenida para diagnóstico. Requisitos clave: indicación precisa, orientación (ej. ayuno de 8-12h para lípidos), evitar ejercicio físico intenso, consentimiento informado (bioética). Laboratorios: Anatomía Patológica (biopsias, citologías, necropsias), Lab Clínico (fluidos), Genética (cromosomas, cariotipo), Imagenología (ecografía, TAC, RMN sin radiación perjudicial).",
                    focusPoints = listOf(
                        "Diferencia entre Biopsia Incisional (extirpación de fragmento) y Excisional (extirpación completa de lesión pequeña con margen > 1cm).",
                        "Los requisitos de toma de muestra y consideraciones bioéticas (consentimiento informado y confidencialidad).",
                        "Tipos de Necropsia: Clínica (muerte natural) vs Médico-Legal (muertes violentas o sospechosas)."
                    ),
                    flashcards = listOf(
                        Flashcard("¿Qué es una biopsia por congelación o transoperatoria?", "Se realiza en el curso de una cirugía para brindar diagnóstico rápido (benigno/maligno) y orientar la conducta quirúrgica inmediata."),
                        Flashcard("¿Qué requisitos analíticos altera el ejercicio físico intenso?", "Modifica parámetros como glucosa, triglicéridos, ácido úrico y urea en sangre."),
                        Flashcard("¿Cuáles son los estudios citogenéticos principales?", "Estudio del cariotipo humano (trastornos cromosómicos), cromatina sexual (cuerpo de Barr) y estudio cromosómico molecular.")
                    )
                ),
                ThemeItem(
                    id = "t2_rev",
                    title = "Tema 2: Lesión Celular Reversible",
                    synthesis = "La lesión reversible ocurre cuando la célula sufre un daño no mortal que desaparece al retirar el estímulo lesivo. Se manifiesta en dos patrones: 1) Tumefacción celular (cambio hidrópico): pérdida de función de las bombas Na-K dependientes de ATP por hipoxia/isquemia, resultando en entrada de agua y sodio a la célula (vacuolas claras de agua en túbulos renales, hígado, miocardio). 2) Metamorfosis grasa (esteatosis): acumulación anómala de triglicéridos en vacuolas de grasa en el hígado (hepatocitos), causada por alcoholismo o dieta grasa. Diagnóstico: elevación de transaminasas (AST/ALT) y ecografía de ecogenicidad aumentada (hígado graso).",
                    focusPoints = listOf(
                        "Mecanismo de la tumefacción celular: falla de la bomba de sodio-potasio por depleción de ATP, causando hinchazón celular con vacuolas claras de agua (negativas a tinción Sudan/Rojo Oleoso y PAS).",
                        "Diferenciación de vacuolas en citoplasma: Grasa se tiñe de anaranjado con Rojo Oleoso o Sudán IV. Glucógeno se tiñe de rosado intenso con PAS (Ácido Periódico de Schiff). Agua no se tiñe (vacuola transparente).",
                        "La isquemia es la causa más frecuente de hipoxia y lesión celular reversible."
                    ),
                    flashcards = listOf(
                        Flashcard("¿Qué produce el fallo de la bomba de sodio-potasio en la hipoxia?", "Aumento del sodio intracelular, salida de potasio y entrada pasiva de agua, provocando hinchazón celular o vacuolización hidrópica."),
                        Flashcard("¿Cuáles son los órganos más afectados por la metamorfosis grasa?", "El hígado y el corazón, ya que son los órganos con mayor dependencia y procesamiento del metabolismo lipídico."),
                        Flashcard("¿Cómo se observa el hígado con esteatosis en una ecografía?", "Se observa un hígado aumentado de tamaño con ecogenicidad aumentada (imagen de color blanquecino brillante).")
                    )
                ),
                ThemeItem(
                    id = "t2_irrev",
                    title = "Tema 2: Lesión Irreversible y Muerte Celular",
                    synthesis = "La persistencia del daño celular progresa a la lesión irreversible, caracterizada por muerte celular: Necrosis (patológica, digestión enzimática y desnaturalización proteica, induce inflamación) o Apoptosis (programada, eliminación de células innecesarias, sin inflamación). Patrones de Necrosis: Coagulativa (desnaturalización proteica por isquemia en miocardio/riñón; microscopía: eosinofilia con conservación de contornos celulares o 'imagen en fantasma'); Licuefactiva (digestión enzimática rápida por autólisis/heterólisis en cerebro por isquemia); Caseosa (aspecto de queso seco, Mycobacterium tuberculosis, rodeada por granuloma de células epitelioides y gigantes); Enzimática de las grasas (pancreatitis aguda, liberación de lipasas que saponifican grasas formando focos calcáreos blanquecinos 'jabones de calcio').",
                    focusPoints = listOf(
                        "Diferenciar cambios nucleares de necrosis: Picnosis (encogimiento y basofilia), Cariolisis (desvanecimiento de cromatina), Cariorrexis (fragmentación nuclear).",
                        "Consecuencias de la necrosis dependen de la extensión, importancia funcional del órgano, reserva funcional y capacidad de regeneración celular.",
                        "Apoptosis se caracteriza por constricción celular, condensación de cromatina bajo la membrana nuclear, formación de cuerpos apoptóticos y fagocitosis rápida sin liberar contenido al espacio extracelular, evitando la inflamación."
                    ),
                    flashcards = listOf(
                        Flashcard("¿Cuál es la causa típica de la necrosis colicuativa o licuefactiva?", "Isquemia o infarto en el sistema nervioso central (cerebro) y en infecciones bacterianas purulentas focalizadas (abscesos)."),
                        Flashcard("¿Qué tipo de necrosis es característica de la tuberculosis?", "La necrosis caseosa, que macroscópicamente parece queso seco granular y microscópicamente carece de contornos celulares y está rodeada por una pared inflamatoria granulomatosa."),
                        Flashcard("¿Cuáles son los tres factores de los que dependen las consecuencias de la necrosis?", "La extensión, la importancia funcional del órgano y la capacidad de regeneración de las células no lesionadas.")
                    )
                ),
                ThemeItem(
                    id = "t2_acum",
                    title = "Tema 2: Acumulaciones Intracelulares y Calcificaciones",
                    synthesis = "Las acumulaciones intracelulares resultan de alteraciones metabólicas. Tipos: 1) Constituyentes normales en exceso (agua en tumefacción, grasa en esteatosis, glucógeno en diabetes, colesterol en ateromas). 2) Sustancias anormales endógenas (alfa-1 antitripsina en cirrosis) o exógenas (antracosis por carbón en pulmón). Aterosclerosis: formación de placas lipídicas (ateromas) con cristales de colesterol y macrófagos espumosos en la íntima de arterias de gran/mediano calibre. Diabetes Mellitus: tipo 1 (déficit absoluto de insulina, destrucción de células beta) y tipo 2 (resistencia a insulina y obesidad); causa microangiopatía con glomeruloesclerosis nodular de Kimmelstiel-Wilson en riñón. Calcificación Patológica: Distrófica (depósito de calcio en tejidos muertos/degenerados con niveles de calcio sérico normales, ej. válvulas cardiacas, tuberculosis caseosa) vs Metastásica (depósito en tejidos sanos secundario a hipercalcemia, ej. por hiperparatiroidismo o intoxicación por vitamina D).",
                    focusPoints = listOf(
                        "La placa de ateroma se localiza en la íntima de arterias y sus complicaciones incluyen ulceración, trombosis, hemorragia, calcificación y dilatación aneurismática.",
                        "Lesión patognomónica de la diabetes en el riñón: Glomeruloesclerosis nodular de Kimmelstiel-Wilson.",
                        "Calcificación distrófica ocurre en zonas de necrosis o ateromas con calcemia normal; calcificación metastásica ocurre en intersticio de pulmones, riñón y mucosa gástrica por hipercalcemia."
                    ),
                    flashcards = listOf(
                        Flashcard("¿Qué son las células espumosas?", "Son macrófagos cargados de lípidos (colesterol) que fagocitan restos celulares grasos en la placa de ateroma o en estados hiperglicémicos."),
                        Flashcard("¿Cuáles son los órganos diana de la calcificación metastásica?", "Los riñones, los pulmones, las arterias sistémicas y la mucosa gástrica, debido a su ambiente alcalino que favorece el depósito de calcio."),
                        Flashcard("¿Qué causa la antracosis?", "La acumulación exógena de partículas de carbón inhaladas de la atmósfera urbana, acumulándose en los macrófagos alveolares y ganglios linfáticos pulmonares sin causar inflamación marcada.")
                    )
                )
            )
        ),
        StudyBlock(
            id = 2,
            title = "Bloque 2: Inflamación y Reparación Tisular",
            priority = "Muy Alta Priority (Core Clinical)",
            hours = "12 Horas",
            description = "Estudio del proceso inflamatorio agudo y crónico, sus mediadores químicos, patrones morfológicos y los mecanismos de curación y cicatrización.",
            themes = listOf(
                ThemeItem(
                    id = "t3_infl",
                    title = "Tema 3: Respuesta Inflamatoria Aguda y Crónica",
                    synthesis = "La inflamación es una respuesta protectora inespecífica que busca eliminar la causa inicial de la lesión. Inflamación Aguda: inicio rápido (minutos/horas), polimorfonucleares neutrófilos, exudación de líquido y proteínas. Signos cardinales: rubor, tumor (edema), calor, dolor e impotencia funcional. Acontecimientos celulares: marginación, rodamiento, adhesión (pavimentación), migración (diapédesis), quimiotaxis y fagocitosis. Inflamación Crónica: inicio lento, semanas o años, infiltrado de células mononucleares (linfocitos, macrófagos, células plasmáticas), destrucción tisular y reparación simultánea por fibrosis y angiogénesis. Mediadores químicos: histamina (primer mediador, vasodilatación, procede de mastocitos). Patrones: seroso (derrame acuoso), fibrinoso (exudado de fibrina, ej. pericarditis 'pan con mantequilla'), supurativo/purulento (abundantes neutrófilos y detritus, ej. meningitis, apendicitis), granulomatoso (acumulación de macrófagos epitelioides y células gigantes, típico de tuberculosis).",
                    focusPoints = listOf(
                        "Diferencias morfológicas y celulares entre inflamación aguda (neutrófilos, exudado) y crónica (mononucleares, macrófagos, fibrosis, angiogénesis).",
                        "Los acontecimientos celulares leucocitarios en orden secuencial: Marginación -> Rodamiento -> Adhesión -> Migración -> Quimiotaxis -> Fagocitosis.",
                        "Evidencias analíticas de fase aguda: leucocitosis (neutrofilia en infecciones bacterianas, linfocitosis en virales, eosinofilia en parasitarias), aumento de la velocidad de sedimentación globular (VSG) y proteínas de fase aguda como la Proteína C Reactiva (PCR) y fibrinógeno."
                    ),
                    flashcards = listOf(
                        Flashcard("¿Qué mediador químico se considera el iniciador de la respuesta vascular en la inflamación aguda?", "La histamina, liberada de inmediato por los mastocitos, basófilos y plaquetas, que induce vasodilatación y aumento de la permeabilidad vascular."),
                        Flashcard("¿Cómo se define el granuloma?", "Un patrón de inflamación crónica caracterizado por la acumulación focal de macrófagos activados (células epitelioides), rodeados de linfocitos y con presencia frecuente de células gigantes multinucleadas de tipo Langhans."),
                        Flashcard("¿Qué tipo de leucocitos se eleva típicamente en una infección bacteriana aguda?", "Los polimorfonucleares neutrófilos (neutrofilia), reflejado en un aumento en el recuento del leucograma.")
                    )
                ),
                ThemeItem(
                    id = "t4_rep",
                    title = "Tema 4: Reparación Tisular y Cicatrización",
                    synthesis = "La reparación restituye la continuidad anatómica y funcional de un tejido dañado. Se da por: 1) Regeneración parenquimatosa: sustitución por células del mismo tipo, ocurre en células lábiles (piel, mucosas, médula ósea) o estables (hígado, túbulos renales, fibroblastos) siempre que el estroma/membrana basal esté intacto. 2) Cicatrización: sustitución por tejido fibroso de colágeno, ocurre cuando el tejido no tiene capacidad de división (células permanentes: neuronas, miocardio) o el estroma está severamente destruido. Requiere tejido de granulación (fibroblastos y neovasos/angiogénesis activada por VEGF y TGF-beta). Cicatrización por Primera Intención: heridas limpias con bordes afrontados, mínima cicatriz. Segunda Intención: heridas extensas, úlceras o quemaduras; requiere contracción de la herida mediada por miofibroblastos y genera una cicatriz abundante. Factores que retardan: Locales (infección - principal, movimiento, cuerpos extraños, tejido necrótico) y Generales/Sistémicos (diabetes mellitus, malnutrición/déficit de vitamina C, corticoesteroides).",
                    focusPoints = listOf(
                        "Clasificación celular según capacidad de regeneración: Lábiles (continuamente en ciclo), Estables (entran al ciclo ante estímulos) y Permanentes (sin regeneración posnatal, reparan solo por cicatrización/fibrosis).",
                        "Composición histológica del tejido de granulación: proliferación de vasos sanguíneos neoformados (angiogénesis) y fibroblastos en una matriz edematosa provisional.",
                        "Diferencias entre cicatrización por primera y segunda intención, y complicaciones de la reparación (dehiscencia de suturas, queloides, cicatriz hipertrófica, contracturas)."
                    ),
                    flashcards = listOf(
                        Flashcard("¿Qué tipo de reparación celular ocurre en un infarto de miocardio?", "Cicatrización (fibrosis), ya que las células miocárdicas son permanentes y no pueden dividirse para regenerar el tejido muscular."),
                        Flashcard("¿Por qué el déficit de vitamina C o de proteínas retarda la cicatrización?", "Porque inhiben la síntesis de colágeno por parte de los fibroblastos, retrasando la maduración de la cicatriz."),
                        Flashcard("¿Cuál es la diferencia entre queloide y cicatriz hipertrófica?", "El queloide se produce por una formación excesiva y persistente de colágeno que sobresale más allá de los límites de la herida original, mientras que la hipertrófica se mantiene dentro de los límites de la lesión.")
                    )
                )
            )
        ),
        StudyBlock(
            id = 3,
            title = "Bloque 3: Genética Clínica I - Herencia y Mutaciones",
            priority = "Alta Priority",
            hours = "12 Horas",
            description = "Fundamentos de genética molecular y patrones de herencia mendeliana monogénica, así como fenómenos biológicos no clásicos de transmisión.",
            themes = listOf(
                ThemeItem(
                    id = "t5_mendel",
                    title = "Tema 5: Enfermedades Monogénicas y Herencia Mendeliana",
                    synthesis = "Las enfermedades monogénicas obedecen a mutaciones en un solo gen y siguen los patrones de herencia mendeliana clasificados según la localización del gen (autonómico o sexual) y la dosis génica (dominante o recesiva). 1) Autosómica Dominante: se expresa en heterocigosis (Pp), hombres y mujeres afectados por igual, patrón vertical (afectados en todas las generaciones), riesgo de recurrencia del 50% (ej. polidactilia, acondroplasia). 2) Autosómica Recesiva: requiere doble dosis (aa), padres portadores sanos (Aa), patrón horizontal (hermanos afectados, padres sanos), riesgo de recurrencia del 25%, aumenta con consanguinidad (ej. albinismo oculocutáneo, fibrosis quística). 3) Ligada al X Recesiva: afecta principalmente a hombres hemicigotos (XhY), mujeres portadoras heterocigotas sanas (XHXh), un hombre enfermo tiene 100% hijas portadoras y 100% hijos sanos; mujer portadora transmite a 50% de hijos varones la enfermedad (ej. hemofilia A, distrofia muscular de Duchenne). 4) Ligada al X Dominante: mujeres afectadas con predominio, un hombre afectado transmite al 100% de sus hijas la enfermedad y 0% de sus hijos varones (ej. raquitismo hipofosfatémico).",
                    focusPoints = listOf(
                        "Criterios de identificación de cada patrón de herencia mendeliana en un árbol genealógico o pedigrí clínico.",
                        "Definición y cálculo de riesgos de recurrencia en parejas portadoras (ej. 25% para autosómica recesiva, 50% para autosómica dominante).",
                        "La hemofilia A es causada por déficit en el factor VIII de coagulación; la sicklemia se debe a sustitución de ácido glutámico por valina en la posición 6 de la cadena beta de hemoglobina."
                    ),
                    flashcards = listOf(
                        Flashcard("¿Por qué las enfermedades recesivas ligadas al X se expresan casi exclusivamente en hombres?", "Porque los hombres tienen un solo cromosoma X (hemicigotos); si heredan el alelo mutado, no tienen otro alelo normal para compensar su función."),
                        Flashcard("¿Qué es un portador sano en herencia autosómica recesiva?", "Un individuo heterocigoto (Aa) que porta el alelo mutado recesivo (a) pero no expresa la enfermedad porque posee un alelo dominante normal (A)."),
                        Flashcard("¿Cuál es el riesgo de recurrencia para una pareja donde uno de los padres está afectado por polidactilia (heterocigoto Pp) y el otro es normal (pp)?", "El riesgo de recurrencia es del 50% para cada embarazo.")
                    )
                ),
                ThemeItem(
                    id = "t5_interf",
                    title = "Tema 5: Interferencias Biológicas en la Herencia",
                    synthesis = "Existen fenómenos biológicos que complican e interfieren con el análisis de los patrones clásicos mendelianos: 1) Penetrancia reducida: un genotipo patológico no se expresa fenotípicamente en el 100% de los portadores (ej. padres sanos que transmiten polidactilia). 2) Expresividad variable: variación en la intensidad o forma de la expresión clínica de un gen entre diferentes personas (ej. polidactilia en una mano vs en las cuatro extremidades). 3) Inactivación del cromosoma X (hipótesis de Lyon): ocurre en el estado de mórula en mujeres, inactivando al azar uno de los cromosomas X, produciendo compensación de dosis. 4) Herencia Mitocondrial: transmisión exclusiva materna por el óvulo; las madres transmiten a todos sus hijos, los padres afectados nunca la transmiten (ej. neuropatía óptica de Leber). 5) Mutaciones Dinámicas: expansión inestable de tripletes de nucleótidos que se agravan y aparecen más temprano en las siguientes generaciones (anticipación), ej. Síndrome del X Frágil (expansión CGG > 200 repeticiones, fenotipo con macroorquidismo). 6) Impronta Genómica: inactivación selectiva materna o paterna de un alelo; ej. deleción del brazo largo del cromosoma 15 da Síndrome de Angelman si se hereda de la madre, o Prader-Willi si se hereda del padre. 7) Disomía Uniparental: heredar ambos cromosomas de un par de un solo progenitor.",
                    focusPoints = listOf(
                        "Diferenciar herencia mitocondrial (transmisión materna estricta a toda la descendencia, hombres no transmiten) de herencia ligada al X.",
                        "El fenómeno de anticipación en mutaciones dinámicas (ej. Síndrome del X Frágil) y el genotipo del síndrome (CGG > 200 repetidos).",
                        "Los síndromes de Angelman (marcha atáxica, ánimo alegre, epilepsia) y Prader-Willi (estatura baja, obesidad, hipogonadismo) como prototipos de impronta genómica en el cromosoma 15."
                    ),
                    flashcards = listOf(
                        Flashcard("¿Por qué un padre afectado por una mutación mitocondrial nunca transmite la enfermedad a sus hijos?", "Porque el espermatozoide solo aporta el pronúcleo masculino en la fecundación y no aporta mitocondrias; todas las mitocondrias del cigoto provienen del citoplasma del óvulo materno."),
                        Flashcard("¿Qué es la penetrancia reducida de un gen?", "Es el porcentaje de individuos con un genotipo patológico que expresan clínicamente la enfermedad. Si es menor al 100%, se dice que la penetrancia es reducida o incompleta."),
                        Flashcard("¿Qué caracteriza clínicamente al Síndrome de Martin Bell o X Frágil?", "Retraso mental, orejas grandes, mandíbula prominente y macroorquidismo (aumento del tamaño de los testículos) en varones pospuberales.")
                    )
                )
            )
        ),
        StudyBlock(
            id = 4,
            title = "Bloque 4: Genética Clínica II - Aberraciones y Poblaciones",
            priority = "Alta Priority",
            hours = "12 Horas",
            description = "Estudio de las aberraciones cromosómicas estructurales y numéricas, diagnóstico prenatal y los principios de la genética de poblaciones.",
            themes = listOf(
                ThemeItem(
                    id = "t5_aberr",
                    title = "Tema 5: Aberraciones Cromosómicas y Citogenética",
                    synthesis = "Las aberraciones cromosómicas son alteraciones en el número o estructura de los cromosomas, diagnosticadas por elección mediante el estudio del Cariotipo. 1) Numéricas: Aneuploidías (no disyunción o retraso en anafase de la meiosis o mitosis). Trisomías autosómicas comunes: Síndrome de Down (trisomía 21, 47,XX/XY +21, retraso mental, fisuras palpebrales inclinadas, pliegue palmar único), Edwards (trisomía 18, malformaciones graves), Patau (trisomía 13). Aneuploidías sexuales: Síndrome de Turner (monosomía sexual 45,XO; corpúsculos de Barr = 0, talla baja, cuello alado, infantilismo sexual), Síndrome de Klinefelter (trisomía sexual 47,XXY; corpúsculos de Barr = 1, hombres de talla alta, testículos pequeños, ginecomastia, infertilidad). Poliploidías: múltiplos exactos del número haploide (3n triploidía, 4n tetraploidía), inviables en humanos. 2) Estructurales: rupturas cromosómicas con reordenamiento. Balanceadas (sin pérdida ni ganancia de material, ej. inversiones, translocaciones recíprocas) vs No Balanceadas (con ganancia/pérdida de material, ej. deleciones como Cri-Du-Chat 5p- con llanto de gato, duplicaciones, isocromosomas).",
                    focusPoints = listOf(
                        "Mecanismo de producción de las aneuploidías: no disyunción o falta de separación de cromosomas homólogos durante la anafase de la meiosis I o II.",
                        "Fórmulas cromosómicas de los síndromes estudiados: Down (47,XX/XY,+21), Turner (45,X), Klinefelter (47,XXY), Edwards (47,XX/XY,+18).",
                        "El corpúsculo de Barr representa un cromosoma X condensado/inactivo en la interfase de células femeninas; su número es igual al número total de cromosomas X menos 1 (Fórmula: X-1)."
                    ),
                    flashcards = listOf(
                        Flashcard("¿Cómo se calcula el número de corpúsculos de Barr en un paciente con Síndrome de Klinefelter (47,XXY)?", "Se calcula con la fórmula X-1. Al tener dos cromosomas X, presenta 2 - 1 = 1 corpúsculo de Barr en el raspado de mucosa oral."),
                        Flashcard("¿Cuál es la causa cromosómica del Síndrome de Cri-Du-Chat?", "Una deleción terminal o intersticial en el brazo corto del cromosoma 5 (5p-), caracterizado por microcefalia, hipotonía y llanto de tono agudo similar al maullido de un gato."),
                        Flashcard("¿Qué es un isocromosoma?", "Un cromosoma anormal formado por la división transversal del centrómero en lugar de longitudinal, lo que resulta en un cromosoma que consta solo de dos brazos cortos o dos brazos largos.")
                    )
                ),
                ThemeItem(
                    id = "t5_pobl",
                    title = "Tema 5: Marcadores Genéticos y Genética de Poblaciones",
                    synthesis = "Los marcadores genéticos son loci con alelos fácilmente identificables, con herencia simple, alta frecuencia y estables ante el ambiente. Ejemplos: 1) Sistema ABO: alelos múltiples (A, B dominantes sobre O; A y B codominantes entre sí). Conflicto ABO neonatal ocurre por incompatibilidad materno-fetal (madre O, feto A o B), pudiendo provocar kerníctero fatal por aumento de bilirrubina. 2) Sistema Rh: antígeno D (Rh positivo dominante, DD o Dd) y d (Rh negativo recesivo, dd). 3) Sistema HLA (MHC): alta heterogeneidad alélica en cromosoma 6, crítico para el rechazo de trasplantes. 4) Marcadores moleculares (RFLPs). Genética de Poblaciones: estudia la distribución y frecuencias de genes y genotipos en poblaciones. Ley de Hardy-Weinberg enuncia que en una población en equilibrio (muy grande, matrimonios al azar, sin mutación ni selección/migración), las frecuencias génicas y genotípicas se mantienen constantes. Ecuaciones: p + q = 1 (frecuencias de alelos dominante p y recesivo q) y p^2 + 2pq + q^2 = 1 (frecuencias genotípicas de homocigoto dominante p^2, heterocigoto 2pq y homocigoto recesivo q^2).",
                    focusPoints = listOf(
                        "Características fundamentales de un marcador genético: patrón de herencia simple, fácil identificación, polimorfismo y nula alteración ambiental.",
                        "Análisis genético del cruzamiento ABO y Rh para determinar la descendencia de una pareja (ej. padres heterocigotos AO y BO pueden tener un hijo OO grupo O).",
                        "Las ecuaciones de Hardy-Weinberg (p + q = 1 y p^2 + 2pq + q^2 = 1) y las cuatro condiciones estrictas para el equilibrio de la ley."
                    ),
                    flashcards = listOf(
                        Flashcard("¿Cuáles son las 4 condiciones para que se cumpla la Ley de Hardy-Weinberg?", "1) Poblaciones muy grandes, 2) Matrimonios al azar (panmixia), 3) Tasa de mutación constante y 4) Que no existan factores de selección natural ni migración."),
                        Flashcard("¿Qué genotipos corresponden a los grupos sanguíneos A y B?", "Grupo A puede ser homocigoto dominante (AA) o heterocigoto (AO). Grupo B puede ser homocigoto dominante (BB) o heterocigoto (BO)."),
                        Flashcard("¿Qué representa '2pq' en la ecuación de Hardy-Weinberg?", "Representa la frecuencia genotípica de los individuos heterocigotos (Aa) en la población en equilibrio.")
                    )
                )
            )
        )
    )

    fun getThemeById(themeId: String): ThemeItem? {
        return blocks.flatMap { it.themes }.firstOrNull { it.id == themeId }
    }
}
