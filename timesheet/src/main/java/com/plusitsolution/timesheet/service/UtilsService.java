package com.plusitsolution.timesheet.service;

import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.plusitsolution.timesheet.entity.OrganizeEntity;
import com.plusitsolution.timesheet.repository.OrganizeRepository;


	
	
	
	
@Service
public class UtilsService {
	@Autowired
	private OrganizeRepository organizeRepository;
	
	
	public String padZeroLeft(String input, int padLength) {
        return padding(input, padLength, '0', false, true);
    }
	
	 public String padding(String input, int padLength, char padChar, boolean isPadRight, boolean isCutOverPadded) {
	        StringBuilder str = new StringBuilder();
	        if (isEmpty(input)) {
	            str.append("");
	        } else {
	            str.append(input);
	        }

	        if (str.length() < padLength) {
	            while(str.length() < padLength) {
	                if (isPadRight) {
	                    str.append(padChar);
	                } else {
	                    str.insert(0, padChar);
	                }
	            }
	        } else if (isCutOverPadded && str.length() > padLength) {
	            if (isPadRight) {
	                str.delete(padLength, str.length());
	            } else {
	                str.delete(0, str.length() - padLength);
	            }
	        }

	        return str.toString();
	    }
	 
	 public boolean isEmpty(String input) {
	        return input == null || input.equals("");
	    }
	 
	 public double round(double amount) {
         return Math.round(amount*100.0)/100.0;
     }
	 
	 public String generateEmpCode(AtomicInteger counter , String orgID) {
		 OrganizeEntity orgEntity = organizeRepository.findById(orgID).get();
		 return orgEntity.getShortName()+padZeroLeft(String.valueOf(counter.getAndAdd(1)), 4);
	 }
	 
	 public String paddding(int counter) {

		 return padZeroLeft(String.valueOf(counter), 2);
	 }
	 
//	 public String generateBasketID(AtomicInteger counter) {
//		 return "B"+padZeroLeft(String.valueOf(counter.getAndAdd(1)), 3);
//	 }
}
