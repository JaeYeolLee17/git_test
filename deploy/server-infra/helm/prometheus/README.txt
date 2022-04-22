** Jenkins values 수정사항

1. persistence volume
   nfs pv, pvc 만들어서 existingClaim 에 pvc 설정.
  
   promethues server, alertmanager 두 개 pvc 를 셋팅해준다. 
   
   특히 promethues server nfs dir 은 runAsUser 가 nouser이므로, 접근권한을 777로 변경.

2. promethrus server
   서비스 타입을 LoadBalanser 타입으로 설정.

3. nginx container metrics 수집을 위해
   - 소스에 nginx.conf 에 /metrics 에 stub_status 기능을 활성화시킨다.
   - nginx app 배포할 deployment 에 annotation 추가.
     nginx app 배포할 deployment - container 에 nginx-prometheus-exporter 추가.

   ** challenge-monitor 배포 참조!! 

