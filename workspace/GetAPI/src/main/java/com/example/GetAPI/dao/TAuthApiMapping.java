package com.example.GetAPI.dao;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
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
@IdClass(TAuthApiMappingId.class)
@Table(
    name = "t_auth_api_mapping",
    schema = "tbl_dtls",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_t_user_usergroupid_api_ep",
            columnNames = {"usergroupid", "api_ep"}
        )
    }
)
public class TAuthApiMapping {

    @Id
    @Column(name = "usergroupid")
    private Long usergroupId;

    @Id
    @Column(name = "api_ep", length = 1000)
    private String apiEp;

    @Column(name = "api_method", nullable = false, length = 50)
    private String apiMethod;

    @Column(name = "ow_json", nullable = false, length = 5000)
    private String owJson;

}