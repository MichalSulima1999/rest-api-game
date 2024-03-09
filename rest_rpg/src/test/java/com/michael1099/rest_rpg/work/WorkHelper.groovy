package com.michael1099.rest_rpg.work

import com.michael1099.rest_rpg.helpers.PageHelper
import com.michael1099.rest_rpg.work.model.Work
import org.openapitools.model.ResourceType
import org.openapitools.model.WorkCreateRequest
import org.openapitools.model.WorkLite
import org.openapitools.model.WorkLitePage
import org.openapitools.model.WorkSearchRequest

class WorkHelper {

    static WorkCreateRequest createWorkCreateRequest(Map customArgs = [:]) {
        Map args = [
                name          : "Chop a tree",
                resourceType  : ResourceType.GOLD,
                resourceAmount: 50,
                workMinutes   : 240
        ]

        args << customArgs

        return new WorkCreateRequest(args.name, args.resourceType, args.resourceAmount, args.workMinutes)
    }

    static WorkSearchRequest createWorkSearchRequest(Map args = [:]) {
        new WorkSearchRequest()
                .pagination(PageHelper.createPagination(args))
                .nameLike(args.nameLike as String)
                .resourceTypeIn(args.resourceTypeIn as List)
                .resourceAmountGreaterThanOrEqual(args.resourceAmountGreaterThanOrEqual as Integer)
                .resourceAmountLessThanOrEqual(args.resourceAmountLessThanOrEqual as Integer)
                .workMinutesGreaterThanOrEqual(args.workMinutesGreaterThanOrEqual as Integer)
                .workMinutesLessThanOrEqual(args.workMinutesLessThanOrEqual as Integer)
    }

    static boolean compare(WorkCreateRequest request, WorkLite lite) {
        assert request.name == lite.name
        assert request.resourceType == lite.resourceType
        assert request.resourceAmount == lite.resourceAmount
        assert request.workMinutes == lite.workMinutes

        true
    }

    static boolean compare(Work work, Work work2) {
        assert work.id == work2.id
        assert work.name == work2.name
        assert work.resourceType == work2.resourceType
        assert work.resourceAmount == work2.resourceAmount
        assert work.workMinutes == work2.workMinutes

        true
    }

    static boolean compare(Work work, WorkLite lite) {
        assert work.id == lite.id
        assert work.name == lite.name
        assert work.resourceType == lite.resourceType
        assert work.resourceAmount == lite.resourceAmount
        assert work.workMinutes == lite.workMinutes

        true
    }

    static boolean compare(List<Work> works, WorkLitePage page) {
        def workLites = page.content
        assert works.size() == workLites.size()
        works = works.sort { it.id }
        workLites = workLites.sort { it.id }
        assert works.withIndex().every { compare(it.v1, workLites[it.v2]) }

        true
    }
}
