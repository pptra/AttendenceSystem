package dao;

import entity.JobType;

public interface JobTypeDao extends Dao<JobType, Long> {

	JobType findByName(String name);

}