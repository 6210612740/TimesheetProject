package com.plusitsolution.timesheet.repository;

import java.util.List;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import com.plusitsolution.timesheet.entity.EmployeeEntity;
import com.plusitsolution.timesheet.entity.HolidayEntity;
import com.plusitsolution.timesheet.entity.MedicalEntity;
@Repository
public interface HolidayRepository extends ElasticsearchRepository<HolidayEntity , String> {
	
	public List<HolidayEntity> findAll();
}