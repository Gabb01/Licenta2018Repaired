package com.licenta2018.spring.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
public class Request{

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	
	@JsonManagedReference
	@ManyToOne
	private RequestType type;
	
	private Date issueDate;
	private Date approvalDate;
	private boolean approvalStatus;
	private boolean rejected;
	
	private String numeSolicitant;
	private String categorieConstructie;
	private double suprafataTeren;
	private String adresaConstructie;
	private String numeProiectant;
	private double valoareLucrari;
	private int nrAutorizatie;
	private String utilitati;
	private int codCaen;
	
	@ManyToOne
	private User user;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public RequestType getType() {
		return type;
	}

	public void setType(RequestType type) {
		this.type = type;
	}

	public Date getIssueDate() {
		return issueDate;
	}

	public void setIssueDate(Date issueDate) {
		this.issueDate = issueDate;
	}

	public Date getApprovalDate() {
		return approvalDate;
	}

	public void setApprovalDate(Date approvalDate) {
		this.approvalDate = approvalDate;
	}

	public boolean isApprovalStatus() {
		return approvalStatus;
	}

	public void setApprovalStatus(boolean approvalStatus) {
		this.approvalStatus = approvalStatus;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getNumeSolicitant() {
		return numeSolicitant;
	}

	public void setNumeSolicitant(String numeSolicitant) {
		this.numeSolicitant = numeSolicitant;
	}

	public String getCategorieConstructie() {
		return categorieConstructie;
	}

	public void setCategorieConstructie(String categorieConstructie) {
		this.categorieConstructie = categorieConstructie;
	}

	public double getSuprafataTeren() {
		return suprafataTeren;
	}

	public void setSuprafataTeren(double suprafataTeren) {
		this.suprafataTeren = suprafataTeren;
	}

	public String getAdresaConstructie() {
		return adresaConstructie;
	}

	public void setAdresaConstructie(String adresaConstructie) {
		this.adresaConstructie = adresaConstructie;
	}

	public String getNumeProiectant() {
		return numeProiectant;
	}

	public void setNumeProiectant(String numeProiectant) {
		this.numeProiectant = numeProiectant;
	}

	public double getValoareLucrari() {
		return valoareLucrari;
	}

	public void setValoareLucrari(double valoareLucrari) {
		this.valoareLucrari = valoareLucrari;
	}

	public int getNrAutorizatie() {
		return nrAutorizatie;
	}

	public void setNrAutorizatie(int nrAutorizatie) {
		this.nrAutorizatie = nrAutorizatie;
	}

	public String getUtilitati() {
		return utilitati;
	}

	public void setUtilitati(String utilitati) {
		this.utilitati = utilitati;
	}

	public int getCodCaen() {
		return codCaen;
	}

	public void setCodCaen(int codCaen) {
		this.codCaen = codCaen;
	}
	
	public boolean isRejected()
	{
		return rejected;
	}
	public void setReject(boolean b) {
		this.rejected = b;
	}

}
