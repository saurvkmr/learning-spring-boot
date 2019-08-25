package com.kumar.learningspringboot.repository;

import com.kumar.learningspringboot.vo.Chapter;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChapterRepository extends ReactiveCrudRepository<Chapter, String> {
}
