PGDMP     $    6        
        {         	   recipe_db    15.1    15.1 -    (           0    0    ENCODING    ENCODING        SET client_encoding = 'UTF8';
                      false            )           0    0 
   STDSTRINGS 
   STDSTRINGS     (   SET standard_conforming_strings = 'on';
                      false            *           0    0 
   SEARCHPATH 
   SEARCHPATH     8   SELECT pg_catalog.set_config('search_path', '', false);
                      false            +           1262    16398 	   recipe_db    DATABASE     }   CREATE DATABASE recipe_db WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE_PROVIDER = libc LOCALE = 'Russian_Russia.1252';
    DROP DATABASE recipe_db;
                postgres    false                        2615    2200    public    SCHEMA     2   -- *not* creating schema, since initdb creates it
 2   -- *not* dropping schema, since initdb creates it
                postgres    false            �            1259    16399    photos    TABLE     �   CREATE TABLE public.photos (
    photo_id integer NOT NULL,
    path character varying NOT NULL,
    sequence integer NOT NULL,
    recipe_id integer NOT NULL,
    recipe_step_id integer
);
    DROP TABLE public.photos;
       public         heap    postgres    false    5            �            1259    16404    photos_photo_id_seq    SEQUENCE     �   CREATE SEQUENCE public.photos_photo_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 *   DROP SEQUENCE public.photos_photo_id_seq;
       public          postgres    false    214    5            ,           0    0    photos_photo_id_seq    SEQUENCE OWNED BY     K   ALTER SEQUENCE public.photos_photo_id_seq OWNED BY public.photos.photo_id;
          public          postgres    false    215            �            1259    16405    recipe_categories    TABLE     k   CREATE TABLE public.recipe_categories (
    rc_id integer NOT NULL,
    name character varying NOT NULL
);
 %   DROP TABLE public.recipe_categories;
       public         heap    postgres    false    5            �            1259    16410    recipe_categories_rc_id_seq    SEQUENCE     �   CREATE SEQUENCE public.recipe_categories_rc_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 2   DROP SEQUENCE public.recipe_categories_rc_id_seq;
       public          postgres    false    216    5            -           0    0    recipe_categories_rc_id_seq    SEQUENCE OWNED BY     [   ALTER SEQUENCE public.recipe_categories_rc_id_seq OWNED BY public.recipe_categories.rc_id;
          public          postgres    false    217            �            1259    16411    recipe_steps    TABLE     �   CREATE TABLE public.recipe_steps (
    rs_id integer NOT NULL,
    recipe_fk integer NOT NULL,
    step integer NOT NULL,
    name character varying,
    description character varying
);
     DROP TABLE public.recipe_steps;
       public         heap    postgres    false    5            �            1259    16416    recipe_steps_rs_id_seq    SEQUENCE     �   CREATE SEQUENCE public.recipe_steps_rs_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 -   DROP SEQUENCE public.recipe_steps_rs_id_seq;
       public          postgres    false    5    218            .           0    0    recipe_steps_rs_id_seq    SEQUENCE OWNED BY     Q   ALTER SEQUENCE public.recipe_steps_rs_id_seq OWNED BY public.recipe_steps.rs_id;
          public          postgres    false    219            �            1259    16417    recipes    TABLE     �   CREATE TABLE public.recipes (
    id integer NOT NULL,
    name character varying(100),
    description text,
    rc_fk integer,
    added_by integer
);
    DROP TABLE public.recipes;
       public         heap    postgres    false    5            �            1259    16422    soups_id_seq    SEQUENCE     �   CREATE SEQUENCE public.soups_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 #   DROP SEQUENCE public.soups_id_seq;
       public          postgres    false    220    5            /           0    0    soups_id_seq    SEQUENCE OWNED BY     ?   ALTER SEQUENCE public.soups_id_seq OWNED BY public.recipes.id;
          public          postgres    false    221            �            1259    16423    users    TABLE     W   CREATE TABLE public.users (
    user_id bigint NOT NULL,
    name character varying
);
    DROP TABLE public.users;
       public         heap    postgres    false    5            x           2604    16471    photos photo_id    DEFAULT     r   ALTER TABLE ONLY public.photos ALTER COLUMN photo_id SET DEFAULT nextval('public.photos_photo_id_seq'::regclass);
 >   ALTER TABLE public.photos ALTER COLUMN photo_id DROP DEFAULT;
       public          postgres    false    215    214            y           2604    16472    recipe_categories rc_id    DEFAULT     �   ALTER TABLE ONLY public.recipe_categories ALTER COLUMN rc_id SET DEFAULT nextval('public.recipe_categories_rc_id_seq'::regclass);
 F   ALTER TABLE public.recipe_categories ALTER COLUMN rc_id DROP DEFAULT;
       public          postgres    false    217    216            z           2604    16473    recipe_steps rs_id    DEFAULT     x   ALTER TABLE ONLY public.recipe_steps ALTER COLUMN rs_id SET DEFAULT nextval('public.recipe_steps_rs_id_seq'::regclass);
 A   ALTER TABLE public.recipe_steps ALTER COLUMN rs_id DROP DEFAULT;
       public          postgres    false    219    218            {           2604    16474 
   recipes id    DEFAULT     f   ALTER TABLE ONLY public.recipes ALTER COLUMN id SET DEFAULT nextval('public.soups_id_seq'::regclass);
 9   ALTER TABLE public.recipes ALTER COLUMN id DROP DEFAULT;
       public          postgres    false    221    220                      0    16399    photos 
   TABLE DATA           U   COPY public.photos (photo_id, path, sequence, recipe_id, recipe_step_id) FROM stdin;
    public          postgres    false    214   2                 0    16405    recipe_categories 
   TABLE DATA           8   COPY public.recipe_categories (rc_id, name) FROM stdin;
    public          postgres    false    216   $2       !          0    16411    recipe_steps 
   TABLE DATA           Q   COPY public.recipe_steps (rs_id, recipe_fk, step, name, description) FROM stdin;
    public          postgres    false    218   ~2       #          0    16417    recipes 
   TABLE DATA           I   COPY public.recipes (id, name, description, rc_fk, added_by) FROM stdin;
    public          postgres    false    220   �2       %          0    16423    users 
   TABLE DATA           .   COPY public.users (user_id, name) FROM stdin;
    public          postgres    false    222   �2       0           0    0    photos_photo_id_seq    SEQUENCE SET     B   SELECT pg_catalog.setval('public.photos_photo_id_seq', 46, true);
          public          postgres    false    215            1           0    0    recipe_categories_rc_id_seq    SEQUENCE SET     J   SELECT pg_catalog.setval('public.recipe_categories_rc_id_seq', 1, false);
          public          postgres    false    217            2           0    0    recipe_steps_rs_id_seq    SEQUENCE SET     E   SELECT pg_catalog.setval('public.recipe_steps_rs_id_seq', 28, true);
          public          postgres    false    219            3           0    0    soups_id_seq    SEQUENCE SET     ;   SELECT pg_catalog.setval('public.soups_id_seq', 74, true);
          public          postgres    false    221            ~           2606    16433    photos photos_pk 
   CONSTRAINT     T   ALTER TABLE ONLY public.photos
    ADD CONSTRAINT photos_pk PRIMARY KEY (photo_id);
 :   ALTER TABLE ONLY public.photos DROP CONSTRAINT photos_pk;
       public            postgres    false    214            �           2606    16435 &   recipe_categories recipe_categories_pk 
   CONSTRAINT     g   ALTER TABLE ONLY public.recipe_categories
    ADD CONSTRAINT recipe_categories_pk PRIMARY KEY (rc_id);
 P   ALTER TABLE ONLY public.recipe_categories DROP CONSTRAINT recipe_categories_pk;
       public            postgres    false    216            �           2606    16437    recipe_steps recipe_steps_pk 
   CONSTRAINT     ]   ALTER TABLE ONLY public.recipe_steps
    ADD CONSTRAINT recipe_steps_pk PRIMARY KEY (rs_id);
 F   ALTER TABLE ONLY public.recipe_steps DROP CONSTRAINT recipe_steps_pk;
       public            postgres    false    218            �           2606    16439    recipes soups_pkey 
   CONSTRAINT     P   ALTER TABLE ONLY public.recipes
    ADD CONSTRAINT soups_pkey PRIMARY KEY (id);
 <   ALTER TABLE ONLY public.recipes DROP CONSTRAINT soups_pkey;
       public            postgres    false    220            �           2606    16441    users users_pk 
   CONSTRAINT     Q   ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pk PRIMARY KEY (user_id);
 8   ALTER TABLE ONLY public.users DROP CONSTRAINT users_pk;
       public            postgres    false    222            |           1259    16442    photos_photo_id_uindex    INDEX     T   CREATE UNIQUE INDEX photos_photo_id_uindex ON public.photos USING btree (photo_id);
 *   DROP INDEX public.photos_photo_id_uindex;
       public            postgres    false    214            �           1259    16443    recipe_categories_rc_id_uindex    INDEX     d   CREATE UNIQUE INDEX recipe_categories_rc_id_uindex ON public.recipe_categories USING btree (rc_id);
 2   DROP INDEX public.recipe_categories_rc_id_uindex;
       public            postgres    false    216            �           1259    16444    recipe_steps_rs_id_uindex    INDEX     Z   CREATE UNIQUE INDEX recipe_steps_rs_id_uindex ON public.recipe_steps USING btree (rs_id);
 -   DROP INDEX public.recipe_steps_rs_id_uindex;
       public            postgres    false    218            �           1259    16445    users_user_id_uindex    INDEX     P   CREATE UNIQUE INDEX users_user_id_uindex ON public.users USING btree (user_id);
 (   DROP INDEX public.users_user_id_uindex;
       public            postgres    false    222            �           2606    16446    photos recipe___fk    FK CONSTRAINT     u   ALTER TABLE ONLY public.photos
    ADD CONSTRAINT recipe___fk FOREIGN KEY (recipe_id) REFERENCES public.recipes(id);
 <   ALTER TABLE ONLY public.photos DROP CONSTRAINT recipe___fk;
       public          postgres    false    220    3206    214            �           2606    16451    photos recipe_step___fk    FK CONSTRAINT     �   ALTER TABLE ONLY public.photos
    ADD CONSTRAINT recipe_step___fk FOREIGN KEY (recipe_step_id) REFERENCES public.recipe_steps(rs_id);
 A   ALTER TABLE ONLY public.photos DROP CONSTRAINT recipe_step___fk;
       public          postgres    false    214    3203    218            �           2606    16456 '   recipe_steps recipe_steps_recipes_id_fk    FK CONSTRAINT     �   ALTER TABLE ONLY public.recipe_steps
    ADD CONSTRAINT recipe_steps_recipes_id_fk FOREIGN KEY (recipe_fk) REFERENCES public.recipes(id);
 Q   ALTER TABLE ONLY public.recipe_steps DROP CONSTRAINT recipe_steps_recipes_id_fk;
       public          postgres    false    218    220    3206            �           2606    16461 %   recipes recipes_recipe_categories__fk    FK CONSTRAINT     �   ALTER TABLE ONLY public.recipes
    ADD CONSTRAINT recipes_recipe_categories__fk FOREIGN KEY (rc_fk) REFERENCES public.recipe_categories(rc_id);
 O   ALTER TABLE ONLY public.recipes DROP CONSTRAINT recipes_recipe_categories__fk;
       public          postgres    false    3200    216    220            �           2606    16466    recipes users___fk    FK CONSTRAINT     w   ALTER TABLE ONLY public.recipes
    ADD CONSTRAINT users___fk FOREIGN KEY (added_by) REFERENCES public.users(user_id);
 <   ALTER TABLE ONLY public.recipes DROP CONSTRAINT users___fk;
       public          postgres    false    220    3208    222                  x������ � �         J   x�? ��1	Супы
2	Завтраки
3	Горячие блюда
\.


�^$]      !      x������ � �      #      x������ � �      %   ?   x�35333�001��L)�..O�O�����K�/JM�,H-�O�/�267240��4�*����� ;;     