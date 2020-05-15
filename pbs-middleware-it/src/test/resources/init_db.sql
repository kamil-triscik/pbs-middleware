INSERT INTO public."user" (id, username, password, first_name, last_name, email, is_disabled, user_role)
    VALUES ('e1091cb5-b284-4e3c-87af-b69f87602e7f', 'user1', '$2a$10$Ku7jSR.grFT6ngAmYnA/e.akkzjjxTkLHLdcv1.P55WWa8nVeUH8u', 'user1', 'new', 'user1@localhost', false, 'ROLE_CUSTOMER')
    ON CONFLICT DO NOTHING;

INSERT INTO public."user" (id, username, password, first_name, last_name, email, is_disabled, user_role)
    VALUES ('970c3724-9771-4932-9601-2b683d978673', 'user2', '$2a$10$Ku7jSR.grFT6ngAmYnA/e.akkzjjxTkLHLdcv1.P55WWa8nVeUH8u', 'user2', 'new', 'user2@localhost', false, 'ROLE_CUSTOMER')
    ON CONFLICT DO NOTHING;

INSERT INTO public."script" (id, name, description, code)
VALUES ('c45ecf15-776d-4a71-b478-464ae576fca5', 'testScript', 'testScriptDescription', 'println()')
ON CONFLICT DO NOTHING;
