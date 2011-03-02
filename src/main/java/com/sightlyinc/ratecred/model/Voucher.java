package com.sightlyinc.ratecred.model;

import java.util.Date;

public class Voucher {
	
	/*"redemption_code": "unique_code",
    "offer_id": "id_of_offer",
    "reservation_id": "id_of_reservation",
    "barcode": "",
    "print_url" "http://voucher_print_url",
    "status": "acquired",
    "expiration_date": "",
    "acquired_at": "YYYY-MM-DD HH:MM",
    "redeemed_at": "YYYY-MM-DD HH:MM",
    "cancelled_at": "YYYY-MM-DD HH:MM"*/
	
	protected Long id;
	protected Integer version = new Integer(0);
	
	private Long timeCreated;
	private Long timeUpdated;
	
	private String redemptionCode;
	private String externalOfferId;
	private String externalSource;
	private String reservationId;
	private String barcode;
	private String printUrl;
	private String status;
	private Long expirationAtDate;
	private Long acquiredAtDate;
	private Long redeemedAtDate;
	private Long cancelledAtDate;

	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Integer getVersion() {
		return version;
	}
	public void setVersion(Integer version) {
		this.version = version;
	}
	
	
	public Long getTimeCreated() {
		return timeCreated;
	}
	public void setTimeCreated(Long timeCreated) {
		this.timeCreated = timeCreated;
	}
	public Long getTimeUpdated() {
		return timeUpdated;
	}
	public void setTimeUpdated(Long timeUpdated) {
		this.timeUpdated = timeUpdated;
	}
	public String getRedemptionCode() {
		return redemptionCode;
	}
	public void setRedemptionCode(String redemptionCode) {
		this.redemptionCode = redemptionCode;
	}
	public String getExternalOfferId() {
		return externalOfferId;
	}
	public void setExternalOfferId(String externalOfferId) {
		this.externalOfferId = externalOfferId;
	}
	
	
	public String getExternalSource() {
		return externalSource;
	}
	public void setExternalSource(String externalSource) {
		this.externalSource = externalSource;
	}
	public String getReservationId() {
		return reservationId;
	}
	public void setReservationId(String reservationId) {
		this.reservationId = reservationId;
	}
	public String getBarcode() {
		return barcode;
	}
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	public String getPrintUrl() {
		return printUrl;
	}
	public void setPrintUrl(String printUrl) {
		this.printUrl = printUrl;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

	public Long getExpirationAtDate() {
		return expirationAtDate;
	}
	public void setExpirationAtDate(Long expirationAtDate) {
		this.expirationAtDate = expirationAtDate;
	}
	public Long getAcquiredAtDate() {
		return acquiredAtDate;
	}
	public void setAcquiredAtDate(Long acquiredAtDate) {
		this.acquiredAtDate = acquiredAtDate;
	}
	public Long getRedeemedAtDate() {
		return redeemedAtDate;
	}
	public void setRedeemedAtDate(Long redeemedAtDate) {
		this.redeemedAtDate = redeemedAtDate;
	}
	public Long getCancelledAtDate() {
		return cancelledAtDate;
	}
	public void setCancelledAtDate(Long cancelledAtDate) {
		this.cancelledAtDate = cancelledAtDate;
	}

	
	

}
