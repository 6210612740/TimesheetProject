package com.plusitsolution.timesheet.repository;

import java.util.List;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import com.plusitsolution.timesheet.entity.EmployeeEntity;
import com.plusitsolution.timesheet.entity.HolidayEntity;

@Repository
public interface EmployeeRepository extends ElasticsearchRepository<EmployeeEntity , String> {
	
	public List<EmployeeEntity> findAll();
	
	public EmployeeEntity findByEmpCode(String empCode);

}
