package com.jask.pomotrack.data.previewparameters

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.jask.pomotrack.data.previewparameters.UserData

class UserDataPreviewParameter: PreviewParameterProvider<UserData> {

    override val values = sequenceOf(

        UserData(
            upperValue = 0,
            lowerValue = 0,
            infoPomoValue = 1,
            infoPomoProgress = 1,
            infoColumnValue = 1,
            infoColumnProgress = 1,
            infoTotalColumnValue = "66",
            pieChartValues = listOf(0.0f, 0.0f),
            lineData = listOf(Triple(0, 0.0, 0.0)),
        )
    )
}