package com.michael1099.rest_rpg.work

import com.michael1099.rest_rpg.work.model.Work
import org.openapitools.model.ResourceType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class WorkServiceHelper {

    @Autowired
    WorkRepository workRepository;

    def clean() {
        workRepository.deleteAll()
    }

    Work saveWork(Map customArgs = [:]) {
        Map args = [
                name          : "Kill bear",
                resourceType  : ResourceType.GOLD,
                resourceAmount: 90,
                workMinutes   : 100,
                deleted       : false
        ]
        args << customArgs

        def work = Work.builder()
                .name(args.name)
                .resourceType(args.resourceType)
                .resourceAmount(args.resourceAmount)
                .workMinutes(args.workMinutes)
                .deleted(args.deleted)
                .build()

        workRepository.save(work)
    }

    Work getWork(long workId) {
        workRepository.findById(workId).orElseThrow()
    }
}
