package com.plusitsolution.timesheet.repository;

import java.util.List;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import com.plusitsolution.timesheet.entity.MedicalEntity;
import com.plusitsolution.timesheet.entity.OrganizeEntity;
@Repository
public interface OrganizeRepository extends ElasticsearchRepository<OrganizeEntity, String>{

	public List<OrganizeEntity> findAll();
	
	public OrganizeEntity findByOrgNameTh(String orgNameTh);
}
