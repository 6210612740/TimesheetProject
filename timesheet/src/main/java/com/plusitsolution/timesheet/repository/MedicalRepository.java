package com.plusitsolution.timesheet.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import com.plusitsolution.timesheet.entity.MedicalEntity;
import com.plusitsolution.timesheet.entity.OrganizeEntity;



@Repository
public interface MedicalRepository extends ElasticsearchRepository<MedicalEntity, String>{
	
	public List<MedicalEntity> findAll();
	
	public List<MedicalEntity> findByOrgID(String orgID);
	

}
