package com.gsmhrm.anything_back.domain.plan.presentation.dto.response;

import com.gsmhrm.anything_back.domain.plan.entity.Plan;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class PlanResponse {

    private String title;

    public static PlanResponse planResponse(Plan plan) {

        return PlanResponse.builder()
                .title(plan.getTitle())
                .build();
    }
}
