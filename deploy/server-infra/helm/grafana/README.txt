** Grafana values 수정사항

1. persistence volume
   nfs pv, pvc 만들어서 existingClaim 에 pvc 설정.

   runAsUser, runAsGroup, fsGroup 를 1000 으로 변경 
   (nfs path 에 쓰기 권한이 있는 host 사용자와 동일하게)

2. service type 을 LoadBalancer 로 변경.

3. adminPassword 설정

4. initChownData enabled 를 false 로 설정

