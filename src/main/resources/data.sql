INSERT INTO p_hospital (
    id, name, address, phone, description,
    open_hour, close_hour, created_by, user_id, is_deleted, created_at, updated_at
) VALUES (
             '11111111-1111-1111-1111-111111111111',
             '서울메디컬센터',
             '서울특별시 강남구 건강로 1길',
             '02-1234-5678',
             '최고의 전문의를 보유한 종합병원입니다.',
             '09:00:00',
             '18:00:00',
             1,
             1,
             false,
             NOW(),
             NOW()
         );

-- 스케쥴 등록 (예: 오전 9시, 10시, 11시 타임에 각각 등록)
INSERT INTO p_schedule (
    id, hospital_id, time, capacity, is_deleted, created_by, created_at, updated_at, updated_by
) VALUES
      (
          'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa',
          '11111111-1111-1111-1111-111111111111',
          '09:00:00',
          20,
          false,
          1,
          NOW(),
          NOW(),
          1
      ),
      (
          'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb',
          '11111111-1111-1111-1111-111111111111',
          '10:00:00',
          40,
          false,
          1,
          NOW(),
          NOW(),
          1
      ),
      (
          'cccccccc-cccc-cccc-cccc-cccccccccccc',
          '11111111-1111-1111-1111-111111111111',
          '11:00:00',
          5,
          false,
          1,
          NOW(),
          NOW(),
          1
      );