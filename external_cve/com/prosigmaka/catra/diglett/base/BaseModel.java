package com.prosigmaka.catra.diglett.base;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@MappedSuperclass
public class BaseModel implements Serializable {

    @JsonIgnore
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_date", updatable = false)
    private Date createdDate;

    @JsonIgnore
    @Column(name = "created_by", updatable = false)
    private String createdBy;

    @JsonIgnore
    @Column(name = "updated_date", insertable = false)
    private Date updatedDate;

    @JsonIgnore
    @Column(name = "updated_by", insertable = false)
    private String updatedBy;

    @JsonIgnore
    @Column(name = "deleted_date", insertable = false)
    private Date deletedDate;

    @JsonIgnore
    @Column(name = "status")
    private Boolean status;

    @PrePersist
    protected void onCreated() {
        this.createdDate = new Date();
    }
}
