package com.axisrooms.hcr.util;

public enum UserNotificationType {

//    RESERVATION_CANCELLED("Your Hotel Booking is Cancelled at ", "/cancelled.ftl"),
//    RESERVATION_MODIFIED("Your Hotel Booking is Modified at ", "/modified.ftl"),
//    RESERVATION_CREATED("Your Hotel Booking is Confirmed at ", "/created.ftl"),
//    RESERVATION_CHECKED_IN("You have Checked in to ", "/modified.ftl"),
//    RESERVATION_CHECKED_OUT("You have Checked out from ", "/modified.ftl"),
//    RESERVATION_EXPIRED("Your On Hold Hotel Booking is Expired at ", "/expired.ftl"),
//    RESERVATION_EXPIRATION_REMINDER("Your On Hold Hotel Booking is Expiring at ", "/reminder.ftl"),
//    RESERVATION_ENQUIRY("Your Hotel Booking Enquiry is Confirmed at  ", "/enquiry.ftl"),
//    RESERVATION_ENQUIRY_REMINDER("Your Hotel Booking Enquiry is Expiring at", "/enquiryReminder.ftl"),
//    RESERVATION_INVOICE("Reservation invoice", "/invoice_email.ftl"),
//    RESERVATION_SPANISH_INVOICE("¡Hola! Tu factura de reserva esta disponible aquí", "/spanish_invoice_email.ftl"),
//    RESERVATION_PRO_FORMA_INVOICE("Reservation invoice", "/pro_forma_invoice_tmpl.ftl"),
//    RESERVATION_VOUCHER("Reservation voucher", "/voucher.ftl"),
//    RESERVATION_SPANISH_VOUCHER("¡Gracias! Tu reserva está confirmada", "/spanish_voucher.ftl"),
//    GROUP_RESERVATION_INVOICE("Group reservation invoice", "/invoiceGroup.ftl"),
//    GROUP_RESERVATION_CREATED("Group reservation created", "/createdGroup.ftl"),
//    GROUP_RESERVATION_MODIFIED("Group reservation modified", "/modifiedGroup.ftl"),
//    GROUP_RESERVATION_CANCELLED("Group reservation cancelled", "/modifiedGroup.ftl"),
//    GROUP_RESERVATION_EXPIRATION_REMINDER("Group reservation expiration reminder", "/groupReminder.ftl"),
    TERMS_AND_CONDITION("Terms And Conditions", "/termsAndConditions.ftl");
//    ROOM_AVAILABILITY_ALERT("Property Wise Room Availability", "/roomAvailabilityAlert.ftl"),
//    PAYMENT_REMINDER("Payment reminder","/paymentReminder.ftl"),
//    CRS_CM_ROOM_AVAILABILITY_MISMATCH_ALERT("Property Wise CRS and CM Room Availability Mismatch", "/crsCmRoomAvailabilityMismatch.ftl"),
//    OVER_BOOKING_ALERT("Overbooking alert", "/overbookingAlert.ftl"),
//    BALANCE_DUE_BOOKING_ALERT("BalanceDue alert", "/balanceDueAlert.ftl");


    public final String value;
    public final String templatePath;

    UserNotificationType(String value, String templatePath) {
        this.value = value;
        this.templatePath = templatePath;
    }

    public String getTemplatePath() {
        return templatePath;
    }

}
