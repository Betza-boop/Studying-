package com.example.data

object PresetQuestions {
    val questions = listOf(
        SimQuestion(
            id = 1,
            type = "CASO_CLINICO",
            questionText = "Paciente masculino de 48 años que es ingresado por presentar un cuadro clínico de diarreas graves. Mediante exámenes complementarios se diagnostica hipopotasemia. Explique:\n1) ¿Qué tipo de respuesta celular adaptativa o lesión reversible se produce en los túbulos contorneados renales debido a la hipopotasemia?\n2) ¿Cuál es el mecanismo de producción de este cuadro morfológico?",
            correctAnswer = "1) Se produce tumefacción celular (o cambio hidrópico / vacuolización hidrópica).\n2) Se debe a la pérdida de función de las bombas iónicas (bomba Na-K dependiente de ATP) en la membrana plasmática, lo que causa entrada pasiva de sodio y agua a la célula.",
            justification = "La tumefacción celular es un cuadro morfológico de lesión celular reversible. Al faltar oxígeno o nutrientes (hipoxia/isquemia/trastornos de electrolitos), se agota el ATP y falla la bomba Na-K. El sodio entra a la célula arrastrando agua, viéndose microscópicamente como vacuolas claras de agua en el citoplasma."
        ),
        SimQuestion(
            id = 2,
            type = "SELECCION",
            questionText = "En el curso de una pancreatitis hemorrágica aguda, el tejido adiposo pancreático y peripancreático sufre destrucción celular. Marque la respuesta correcta sobre el tipo de necrosis que se produce:",
            options = listOf(
                "Necrosis por coagulación por desnaturalización de proteínas.",
                "Necrosis licuefactiva por autólisis rápida.",
                "Necrosis enzimática de las grasas debido a la liberación de lipasas pancreáticas.",
                "Necrosis caseosa típica de micobacterias."
            ),
            correctAnswer = "2", // 0-indexed, so 2 corresponds to "Necrosis enzimática de las grasas..."
            justification = "En la pancreatitis aguda, las lipasas pancreáticas activadas se liberan al tejido adiposo circundante y saponifican las grasas neutras de los adipocitos, formando jabones de calcio insolubles que macroscópicamente se observan como áreas blanco tizas calcáreas."
        ),
        SimQuestion(
            id = 3,
            type = "CASO_CLINICO",
            questionText = "Un paciente masculino de 28 años presenta aumento de volumen en la región cervical (adenopatía). El examen anatomopatológico revela un patrón de respuesta inflamatorio granulomatoso ganglionar, con necrosis caseosa central.\n1) ¿Qué diagnóstico clínico-comunitario de alta sospecha sugiere esta morfología?\n2) ¿Cuál es la célula característica y predominante de este patrón inflamatorio?",
            correctAnswer = "1) Sugiere sospecha de tuberculosis ganglionar (linfadenitis tuberculosa).\n2) La célula epitelioide (o macrófago modificado/activado), acompañada de linfocitos y células gigantes multinucleadas (tipo Langhans).",
            justification = "La presencia de necrosis caseosa central rodeada de una pared inflamatoria granulomatosa es el hallazgo clásico y patognomónico de la tuberculosis. La célula clave del granuloma es el macrófago activado, también llamado célula epitelioide por su parecido histológico al epitelio."
        ),
        SimQuestion(
            id = 4,
            type = "SELECCION",
            questionText = "Una paciente de 66 años con diabetes mellitus y varices presenta una úlcera en la pierna derecha con abundante tejido de granulación exuberante. Respecto al proceso de cicatrización en esta paciente, marque la opción correcta:",
            options = listOf(
                "La úlcera cicatrizará por primera intención debido al buen estado circulatorio.",
                "Los esteroides acelerarían el proceso de cicatrización al aumentar la síntesis de colágeno.",
                "La diabetes mellitus y el mal estado circulatorio son factores sistémicos que retardan la cicatrización.",
                "El tejido de granulación exuberante favorece una cicatrización limpia y rápida."
            ),
            correctAnswer = "2", // 2 corresponds to "La diabetes mellitus y el mal estado circulatorio son factores sistémicos..."
            justification = "Tanto la diabetes mellitus (factor metabólico general) como la insuficiencia vascular/varices (factor local de mal estado circulatorio) retardan drásticamente la migración de fibroblastos y la síntesis de colágeno, enlenteciendo la curación de heridas."
        ),
        SimQuestion(
            id = 5,
            type = "DESARROLLO",
            questionText = "Describa la fórmula cromosómica y el número de corpúsculos de Barr en células en interfase correspondientes a:\n1) Síndrome de Turner.\n2) Síndrome de Klinefelter.",
            correctAnswer = "1) Síndrome de Turner: Fórmula 45,XO (ó 45,X). Corpúsculos de Barr: 0 (cero).\n2) Síndrome de Klinefelter: Fórmula 47,XXY. Corpúsculos de Barr: 1 (uno).",
            justification = "El número de corpúsculos de Barr (cromatina sexual condensada) en interfase es siempre igual al número de cromosomas X menos 1 (Fórmula: X-1). El Síndrome de Turner tiene un solo X (1-1=0). El Síndrome de Klinefelter tiene dos X (2-1=1)."
        ),
        SimQuestion(
            id = 6,
            type = "DESARROLLO",
            questionText = "En una comunidad atendida por el Consultorio de Barrio Adentro, se estudia una población de 1000 personas para la frecuencia del antígeno Rh. Si se determina que el 70% de las personas son Rh positivas (fenotipo dominante), y la población está en equilibrio de Hardy-Weinberg (p + q = 1, p^2 + 2pq + q^2 = 1):\n1) ¿Cuál es la frecuencia genotípica de los homocigóticos recesivos (dd)?\n2) ¿Cuál es la frecuencia del alelo recesivo d (q)?",
            correctAnswer = "1) Frecuencia genotípica dd (q^2) = 0.30 (30%).\n2) Frecuencia del alelo d (q) = raíz cuadrada de 0.30 ≈ 0.548 (54.8%).",
            justification = "Si el 70% presenta el fenotipo dominante (Rh positivo), el 30% restante corresponde obligatoriamente al fenotipo recesivo (Rh negativo, genotipo dd). Según la Ley de Hardy-Weinberg, la frecuencia de dd es q^2, por lo que q^2 = 0.30, y q es la raíz cuadrada de 0.30, es decir, aproximadamente 0.55."
        )
    )
}
