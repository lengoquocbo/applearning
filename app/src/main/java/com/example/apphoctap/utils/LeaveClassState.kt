sealed class LeaveClassState {
    object Idle : LeaveClassState()
    object Loading : LeaveClassState()
    object Success : LeaveClassState()
    data class Error(val message: String, val classId: String = "") : LeaveClassState()
}