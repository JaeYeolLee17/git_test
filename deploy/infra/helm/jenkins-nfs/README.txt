** Jenkins values 수정사항

1. persistence volume
   nfs pv, pvc 만들어서 existingClaim 에 pvc 설정.

2. service account
   create: false
   name 에 sa 만들어서 설정.

3. agent memory limit 상향.
   기본 설정으로는 memory 부족하여 gradle build 시 gradle deamon 강제 중단됨.

4. agent workspace volume
   workspaceVolume 에 nfs 로 설정

4. build script 작성 시
   gradle build 시 gradle user home 을 persistence 위치로 변경해주어야 한다.
   (gradle lib download 받는 시간이 너무 많이 소요되므로.)

   build shell script 에서 
   export GRADLE_USER_HOME=${WORKSPACE}/../.gradle  등으로...


-----------------------
** 위 설정 전 먼저 준비할 사항.

1. nfs 공유 dir 설정.
   infra > nfs 참조.

-----------------------
** 최초 Jenkins 구동 후 plugin 모두 update 필수!!
오류 없을때 까지

