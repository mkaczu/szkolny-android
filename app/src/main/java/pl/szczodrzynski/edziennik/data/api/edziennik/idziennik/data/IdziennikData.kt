/*
 * Copyright (c) Kuba Szczodrzyński 2019-10-25. 
 */

package pl.szczodrzynski.edziennik.data.api.edziennik.idziennik.data

import pl.szczodrzynski.edziennik.R
import pl.szczodrzynski.edziennik.data.api.edziennik.idziennik.*
import pl.szczodrzynski.edziennik.data.api.edziennik.idziennik.data.api.IdziennikApiCurrentRegister
import pl.szczodrzynski.edziennik.data.api.edziennik.idziennik.data.api.IdziennikApiMessagesInbox
import pl.szczodrzynski.edziennik.data.api.edziennik.idziennik.data.api.IdziennikApiMessagesSent
import pl.szczodrzynski.edziennik.data.api.edziennik.idziennik.data.web.*
import pl.szczodrzynski.edziennik.utils.Utils

class IdziennikData(val data: DataIdziennik, val onSuccess: () -> Unit) {
    companion object {
        private const val TAG = "IdziennikData"
    }

    init {
        nextEndpoint(onSuccess)
    }

    private fun nextEndpoint(onSuccess: () -> Unit) {
        if (data.targetEndpointIds.isEmpty()) {
            onSuccess()
            return
        }
        if (data.cancelled) {
            onSuccess()
            return
        }
        val id = data.targetEndpointIds.firstKey()
        data.targetEndpointIds.remove(id)
        useEndpoint(id) { endpointId ->
            data.progress(data.progressStep)
            nextEndpoint(onSuccess)
        }
    }

    private fun useEndpoint(endpointId: Int, onSuccess: (endpointId: Int) -> Unit) {
        val lastSync = data.targetEndpointIds[endpointId]
        Utils.d(TAG, "Using endpoint $endpointId. Last sync time = $lastSync")
        when (endpointId) {
            ENDPOINT_IDZIENNIK_WEB_TIMETABLE -> {
                data.startProgress(R.string.edziennik_progress_endpoint_timetable)
                IdziennikWebTimetable(data, lastSync, onSuccess)
            }
            ENDPOINT_IDZIENNIK_WEB_GRADES -> {
                data.startProgress(R.string.edziennik_progress_endpoint_grades)
                IdziennikWebGrades(data, lastSync, onSuccess)
            }
            ENDPOINT_IDZIENNIK_WEB_PROPOSED_GRADES -> {
                data.startProgress(R.string.edziennik_progress_endpoint_proposed_grades)
                IdziennikWebProposedGrades(data, lastSync, onSuccess)
            }
            ENDPOINT_IDZIENNIK_WEB_EXAMS -> {
                data.startProgress(R.string.edziennik_progress_endpoint_exams)
                IdziennikWebExams(data, lastSync, onSuccess)
            }
            ENDPOINT_IDZIENNIK_WEB_HOMEWORK -> {
                data.startProgress(R.string.edziennik_progress_endpoint_homework)
                IdziennikWebHomework(data, lastSync, onSuccess)
            }
            ENDPOINT_IDZIENNIK_WEB_NOTICES -> {
                data.startProgress(R.string.edziennik_progress_endpoint_notices)
                IdziennikWebNotices(data, lastSync, onSuccess)
            }
            ENDPOINT_IDZIENNIK_WEB_ANNOUNCEMENTS -> {
                data.startProgress(R.string.edziennik_progress_endpoint_announcements)
                IdziennikWebAnnouncements(data, lastSync, onSuccess)
            }
            ENDPOINT_IDZIENNIK_WEB_ATTENDANCE -> {
                data.startProgress(R.string.edziennik_progress_endpoint_attendance)
                IdziennikWebAttendance(data, lastSync, onSuccess)
            }
            ENDPOINT_IDZIENNIK_API_CURRENT_REGISTER -> {
                data.startProgress(R.string.edziennik_progress_endpoint_lucky_number)
                IdziennikApiCurrentRegister(data, lastSync, onSuccess)
            }
            ENDPOINT_IDZIENNIK_API_MESSAGES_INBOX -> {
                data.startProgress(R.string.edziennik_progress_endpoint_messages_inbox)
                IdziennikApiMessagesInbox(data, lastSync, onSuccess)
            }
            ENDPOINT_IDZIENNIK_API_MESSAGES_SENT -> {
                data.startProgress(R.string.edziennik_progress_endpoint_messages_outbox)
                IdziennikApiMessagesSent(data, lastSync, onSuccess)
            }
            else -> onSuccess(endpointId)
        }
    }
}
