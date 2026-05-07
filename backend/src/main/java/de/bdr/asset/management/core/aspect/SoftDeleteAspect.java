package de.bdr.asset.management.core.aspect;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.hibernate.Session;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Aspect
@Component
public class SoftDeleteAspect {

    @PersistenceContext
    private EntityManager entityManager;

    @Around("execution(* de.bdr.asset.management.asset.AssetRepository.*(..))")
    public Object applyFilter(ProceedingJoinPoint joinPoint) throws Throwable {
        // Get the current user's role
        boolean isAdmin = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication())
                .getAuthorities().stream()
                .anyMatch(a -> Objects.equals(a.getAuthority(), "ROLE_ADMIN"));

        Session session = entityManager.unwrap(Session.class);

        try {
            // Only enable the filter if the user is NOT an admin
            if (!isAdmin) {
                session.enableFilter("softDeleteFilter")
                        .setParameter("deletedStatus", "DELETED");
            }

            return joinPoint.proceed();

        } finally {
            session.disableFilter("softDeleteFilter");
        }
    }
}
