package com.example.GetAPI.dao;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "t_event_transaction", schema = "tbl_dtls")
public class TEventTransaction {

	@Id
	@Column(name = "event_transaction_id", nullable = false)
	private Long eventTransactionId;

	@Column(name = "usergroupid", nullable = false)
	private Long usergroupId;

	@Column(name = "event_code", length = 100)
	private String eventCode;

	@Column(name = "api_ep", nullable = false, length = 1000)
	private String apiEp;

	@Column(name = "api_method", nullable = false, length = 50)
	private String apiMethod;

	@Column(name = "component_type", nullable = false, length = 200)
	private String componentType;

	@Column(name = "action_type", nullable = false, length = 100)
	private String actionType;

	@Column(name = "execution_type", nullable = false, length = 100)
	private String executionType;

	@Column(name = "decision_content", nullable = false, length = 5000)
	private String decisionContent;
	
}
