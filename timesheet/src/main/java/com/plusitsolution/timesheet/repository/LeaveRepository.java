package com.plusitsolution.timesheet.repository;

import java.util.List;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import com.plusitsolution.timesheet.entity.LeaveEntity;
import com.plusitsolution.timesheet.entity.MedicalEntity;

@Repository
public interface LeaveRepository extends ElasticsearchRepository<LeaveEntity, String>{
	
	public List<LeaveEntity> findAll();
	
	public List<LeaveEntity> findByOrgID(String orgID);
	

}


